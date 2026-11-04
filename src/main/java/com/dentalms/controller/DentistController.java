package com.dentalms.controller;

import com.dentalms.exception.ApiException;
import com.dentalms.model.Dentist;
import com.dentalms.repository.DentistRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dentists")
public class DentistController {

    private final DentistRepository repo;

    public DentistController(DentistRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Dentist> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Dentist get(@PathVariable Long id) { return find(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Dentist create(@Valid @RequestBody Dentist body) { return repo.save(body); }

    @PutMapping("/{id}")
    public Dentist update(@PathVariable Long id, @Valid @RequestBody Dentist body) {
        Dentist d = find(id);
        d.setFirstName(body.getFirstName());
        d.setLastName(body.getLastName());
        d.setPhone(body.getPhone());
        d.setEmail(body.getEmail());
        return repo.save(d);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { repo.delete(find(id)); }

    private Dentist find(Long id) {
        return repo.findById(id).orElseThrow(() ->
                ApiException.notFound("DENTIST_NOT_FOUND", "Dentist does not exist."));
    }
}
