package com.dentalms.dto;

import com.dentalms.model.Appointment;
import com.dentalms.model.AppointmentStatus;
import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        Long patientId, String patientName,
        Long dentistId, String dentistName,
        Long serviceId, String serviceName,
        LocalDateTime startAt, LocalDateTime endAt,
        AppointmentStatus status) {

    public static AppointmentResponse from(Appointment a) {
        return new AppointmentResponse(
                a.getId(),
                a.getPatient().getId(), a.getPatient().getFirstName() + " " + a.getPatient().getLastName(),
                a.getDentist().getId(), a.getDentist().getFirstName() + " " + a.getDentist().getLastName(),
                a.getService().getId(), a.getService().getName(),
                a.getStartAt(), a.getEndAt(), a.getStatus());
    }
}
