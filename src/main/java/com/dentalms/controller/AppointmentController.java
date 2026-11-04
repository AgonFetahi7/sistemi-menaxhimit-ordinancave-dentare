package com.dentalms.controller;

import com.dentalms.dto.AppointmentRequest;
import com.dentalms.dto.AppointmentResponse;
import com.dentalms.model.AppointmentStatus;
import com.dentalms.service.AppointmentService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) { this.service = service; }

    /** F08: filtrim sipas dates, dentistit dhe statusit. Shembull: ?date=2026-11-20&dentistId=1&status=SCHEDULED */
    @GetMapping
    public List<AppointmentResponse> list(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long dentistId,
            @RequestParam(required = false) AppointmentStatus status) {
        return service.search(date, dentistId, status).stream().map(AppointmentResponse::from).toList();
    }

    @GetMapping("/{id}")
    public AppointmentResponse get(@PathVariable Long id) {
        return AppointmentResponse.from(service.get(id));
    }

    /** F04 + F05: rezervim me kontroll orari. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse create(@Valid @RequestBody AppointmentRequest req) {
        return AppointmentResponse.from(service.create(req));
    }

    /** F06: ndryshim i terminit. */
    @PutMapping("/{id}")
    public AppointmentResponse update(@PathVariable Long id, @Valid @RequestBody AppointmentRequest req) {
        return AppointmentResponse.from(service.update(id, req));
    }

    /** F06: anulim (statusi behet CANCELLED, termini nuk fshihet). */
    @PatchMapping("/{id}/cancel")
    public AppointmentResponse cancel(@PathVariable Long id) {
        return AppointmentResponse.from(service.cancel(id));
    }
}
