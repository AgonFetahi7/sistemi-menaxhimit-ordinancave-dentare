package com.dentalms.controller;

import com.dentalms.exception.ApiException;
import com.dentalms.model.DentalService;
import com.dentalms.repository.DentalServiceRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services")
public class DentalServiceController {

    private final DentalServiceRepository repo;

    public DentalServiceController(DentalServiceRepository repo) { this.repo = repo; }

    @GetMapping
    public List<DentalService> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public DentalService get(@PathVariable Long id) { return find(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DentalService create(@Valid @RequestBody DentalService body) { return repo.save(body); }

    @PutMapping("/{id}")
    public DentalService update(@PathVariable Long id, @Valid @RequestBody DentalService body) {
        DentalService s = find(id);
        s.setName(body.getName());
        s.setDescription(body.getDescription());
        s.setDurationMinutes(body.getDurationMinutes());
        return repo.save(s);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { repo.delete(find(id)); }

    private DentalService find(Long id) {
        return repo.findById(id).orElseThrow(() ->
                ApiException.notFound("SERVICE_NOT_FOUND", "Service does not exist."));
    }
}
