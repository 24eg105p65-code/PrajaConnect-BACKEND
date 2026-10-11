package com.prajaconnect.controller;

import com.prajaconnect.dto.Dtos.*;
import com.prajaconnect.model.Complaint;
import com.prajaconnect.service.ComplaintService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService service;

    public ComplaintController(ComplaintService service) {
        this.service = service;
    }

    @GetMapping
    public List<Complaint> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Complaint create(@RequestBody CreateRequest r) {
        return service.create(r);
    }

    @PatchMapping("/{id}/status")
    public Complaint updateStatus(@PathVariable String id, @RequestBody StatusRequest r) {
        return service.updateStatus(id, r);
    }

    @PatchMapping("/{id}/assignment")
    public Complaint assign(@PathVariable String id, @RequestBody AssignRequest r) {
        return service.assign(id, r);
    }

    @PostMapping("/{id}/notes")
    public Complaint addNote(@PathVariable String id, @RequestBody NoteRequest r) {
        return service.addAdminNote(id, r);
    }

    @PostMapping("/{id}/progress")
    public Complaint submitProgress(@PathVariable String id, @RequestBody ProgressRequest r) {
        return service.submitProgress(id, r);
    }

    @PostMapping("/{id}/reply")
    public Complaint reply(@PathVariable String id, @RequestBody ReplyRequest r) {
        return service.citizenReply(id, r);
    }

    @PostMapping("/{id}/feedback")
    public Complaint feedback(@PathVariable String id, @RequestBody FeedbackRequest r) {
        return service.submitFeedback(id, r);
    }
}
