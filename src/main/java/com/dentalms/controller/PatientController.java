package com.dentalms.controller;

import com.dentalms.exception.ApiException;
import com.dentalms.model.Patient;
import com.dentalms.repository.PatientRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientRepository repo;

    public PatientController(PatientRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Patient> list(@RequestParam(required = false) String search) {
        if (search == null || search.isBlank()) return repo.findAll();
        return repo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(search, search);
    }

    @GetMapping("/{id}")
    public Patient get(@PathVariable Long id) { return find(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient create(@Valid @RequestBody Patient body) { return repo.save(body); }

    @PutMapping("/{id}")
    public Patient update(@PathVariable Long id, @Valid @RequestBody Patient body) {
        Patient p = find(id);
        p.setFirstName(body.getFirstName());
        p.setLastName(body.getLastName());
        p.setPhone(body.getPhone());
        p.setEmail(body.getEmail());
        return repo.save(p);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { repo.delete(find(id)); }

    private Patient find(Long id) {
        return repo.findById(id).orElseThrow(() ->
                ApiException.notFound("PATIENT_NOT_FOUND", "Patient does not exist."));
    }
}
