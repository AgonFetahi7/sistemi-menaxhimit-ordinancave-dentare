package com.dentalms.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentRequest(
        @NotNull(message = "patientId is required") Long patientId,
        @NotNull(message = "dentistId is required") Long dentistId,
        @NotNull(message = "serviceId is required") Long serviceId,
        @NotNull(message = "startAt is required")
        @Future(message = "startAt must be in the future") LocalDateTime startAt) {
}
