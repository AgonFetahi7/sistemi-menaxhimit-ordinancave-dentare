package com.dentalms.service;

import com.dentalms.dto.AppointmentRequest;
import com.dentalms.exception.ApiException;
import com.dentalms.model.*;
import com.dentalms.repository.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointments;
    private final PatientRepository patients;
    private final DentistRepository dentists;
    private final DentalServiceRepository services;

    public AppointmentService(AppointmentRepository appointments, PatientRepository patients,
                              DentistRepository dentists, DentalServiceRepository services) {
        this.appointments = appointments;
        this.patients = patients;
        this.dentists = dentists;
        this.services = services;
    }

    @Transactional(readOnly = true)
    public List<Appointment> search(LocalDate date, Long dentistId, AppointmentStatus status) {
        LocalDateTime from = (date != null) ? date.atStartOfDay() : LocalDateTime.of(1900, 1, 1, 0, 0);
        LocalDateTime to = (date != null) ? date.plusDays(1).atStartOfDay() : LocalDateTime.of(3000, 1, 1, 0, 0);
        return appointments.search(from, to, dentistId).stream()
                .filter(a -> status == null || a.getStatus() == status)
                .toList();
    }

    @Transactional(readOnly = true)
    public Appointment get(Long id) {
        return appointments.findById(id).orElseThrow(() ->
                ApiException.notFound("APPOINTMENT_NOT_FOUND", "Appointment does not exist."));
    }

    @Transactional
    public Appointment create(AppointmentRequest req) {
        Appointment a = new Appointment();
        apply(a, req);
        checkNoOverlap(a, -1L);
        return appointments.save(a);
    }

    @Transactional
    public Appointment update(Long id, AppointmentRequest req) {
        Appointment a = get(id);
        if (a.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new ApiException(HttpStatus.CONFLICT, "APPOINTMENT_NOT_EDITABLE",
                    "Only SCHEDULED appointments can be changed.");
        }
        apply(a, req);
        checkNoOverlap(a, id);
        return appointments.save(a);
    }

    @Transactional
    public Appointment cancel(Long id) {
        Appointment a = get(id);
        if (a.getStatus() == AppointmentStatus.COMPLETED) {
            throw new ApiException(HttpStatus.CONFLICT, "APPOINTMENT_NOT_EDITABLE",
                    "A completed appointment cannot be cancelled.");
        }
        a.setStatus(AppointmentStatus.CANCELLED);
        return appointments.save(a);
    }

    /** Gjen entitetet e lidhura, llogarit end_at nga kohezgjatja e sherbimit. */
    private void apply(Appointment a, AppointmentRequest req) {
        Patient p = patients.findById(req.patientId()).orElseThrow(() ->
                ApiException.notFound("PATIENT_NOT_FOUND", "Patient does not exist."));
        Dentist d = dentists.findById(req.dentistId()).orElseThrow(() ->
                ApiException.notFound("DENTIST_NOT_FOUND", "Dentist does not exist."));
        DentalService s = services.findById(req.serviceId()).orElseThrow(() ->
                ApiException.notFound("SERVICE_NOT_FOUND", "Service does not exist."));
        a.setPatient(p);
        a.setDentist(d);
        a.setService(s);
        a.setStartAt(req.startAt());
        a.setEndAt(req.startAt().plusMinutes(s.getDurationMinutes()));
    }

    /** F05 / J02: nje dentist nuk mund te kete dy termine aktive qe perplasen. */
    private void checkNoOverlap(Appointment a, Long excludeId) {
        if (appointments.existsOverlap(a.getDentist().getId(), a.getStartAt(), a.getEndAt(), excludeId)) {
            throw new ApiException(HttpStatus.CONFLICT, "SCHEDULE_CONFLICT",
                    "The dentist already has an appointment in this time slot.");
        }
    }
}
