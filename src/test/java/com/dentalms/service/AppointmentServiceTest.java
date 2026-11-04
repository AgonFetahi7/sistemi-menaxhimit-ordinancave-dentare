package com.dentalms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dentalms.dto.AppointmentRequest;
import com.dentalms.exception.ApiException;
import com.dentalms.model.*;
import com.dentalms.repository.*;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Teste njesi per logjiken e termineve. Nuk kerkojne databaze (repository-t jane mock). */
@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock AppointmentRepository appointments;
    @Mock PatientRepository patients;
    @Mock DentistRepository dentists;
    @Mock DentalServiceRepository services;
    @InjectMocks AppointmentService service;

    private static final LocalDateTime START = LocalDateTime.of(2026, 11, 20, 12, 0);

    private AppointmentRequest request() {
        return new AppointmentRequest(1L, 1L, 1L, START);
    }

    private void stubExistingEntities(int durationMinutes) {
        DentalService s = new DentalService();
        s.setName("Kontroll");
        s.setDurationMinutes(durationMinutes);
        when(patients.findById(1L)).thenReturn(Optional.of(new Patient()));
        when(dentists.findById(1L)).thenReturn(Optional.of(new Dentist()));
        when(services.findById(1L)).thenReturn(Optional.of(s));
    }

    @Test
    void create_calculatesEndAtFromServiceDurationAndSaves() {
        stubExistingEntities(30);
        when(appointments.existsOverlap(any(), any(), any(), any())).thenReturn(false);
        when(appointments.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Appointment result = service.create(request());

        assertEquals(START, result.getStartAt());
        assertEquals(START.plusMinutes(30), result.getEndAt());
        assertEquals(AppointmentStatus.SCHEDULED, result.getStatus());
    }

    @Test
    void create_throwsScheduleConflictAndDoesNotSave() {
        stubExistingEntities(30);
        when(appointments.existsOverlap(any(), any(), any(), any())).thenReturn(true);

        ApiException ex = assertThrows(ApiException.class, () -> service.create(request()));

        assertEquals("SCHEDULE_CONFLICT", ex.getCode());
        assertEquals(409, ex.getStatus().value());
        verify(appointments, never()).save(any());
    }

    @Test
    void create_throwsNotFoundWhenPatientMissing() {
        when(patients.findById(1L)).thenReturn(Optional.empty());

        ApiException ex = assertThrows(ApiException.class, () -> service.create(request()));

        assertEquals("PATIENT_NOT_FOUND", ex.getCode());
        assertEquals(404, ex.getStatus().value());
    }

    @Test
    void cancel_setsStatusToCancelled() {
        Appointment a = new Appointment();
        a.setStatus(AppointmentStatus.SCHEDULED);
        when(appointments.findById(5L)).thenReturn(Optional.of(a));
        when(appointments.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Appointment result = service.cancel(5L);

        assertEquals(AppointmentStatus.CANCELLED, result.getStatus());
    }

    @Test
    void cancel_completedAppointmentIsRejected() {
        Appointment a = new Appointment();
        a.setStatus(AppointmentStatus.COMPLETED);
        when(appointments.findById(5L)).thenReturn(Optional.of(a));

        ApiException ex = assertThrows(ApiException.class, () -> service.cancel(5L));

        assertEquals("APPOINTMENT_NOT_EDITABLE", ex.getCode());
        verify(appointments, never()).save(any());
    }
}
