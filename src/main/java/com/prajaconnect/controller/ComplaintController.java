package com.prajaconnect.controller;

import com.prajaconnect.model.Complaint;
import com.prajaconnect.repository.ComplaintRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintRepository repo;

    public ComplaintController(ComplaintRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Complaint> getAll(@RequestParam(required = false) String status) {
        return status == null ? repo.findAll() : repo.findByStatus(status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getOne(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Complaint create(@Valid @RequestBody Complaint c) {
        c.setId(null);
        return repo.save(c);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Complaint> update(@PathVariable Long id, @Valid @RequestBody Complaint c) {
        return repo.findById(id).map(existing -> {
            existing.setTitle(c.getTitle());
            existing.setDescription(c.getDescription());
            existing.setCategory(c.getCategory());
            existing.setDepartment(c.getDepartment());
            existing.setLocation(c.getLocation());
            existing.setPriority(c.getPriority());
            existing.setStatus(c.getStatus());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Complaint> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return repo.findById(id).map(existing -> {
            existing.setStatus(body.get("status"));
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public Map<String, Long> stats() {
        Map<String, Long> m = new HashMap<>();
        m.put("total", repo.count());
        m.put("pending", (long) repo.findByStatus("PENDING").size());
        m.put("inProgress", (long) repo.findByStatus("IN_PROGRESS").size());
        m.put("resolved", (long) repo.findByStatus("RESOLVED").size());
        return m;
    }
}
