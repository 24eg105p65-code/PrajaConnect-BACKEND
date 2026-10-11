package com.prajaconnect.controller;

import com.prajaconnect.dto.Dtos.ReadRequest;
import com.prajaconnect.model.Notification;
import com.prajaconnect.service.ComplaintService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NotificationController {

    private final ComplaintService service;

    public NotificationController(ComplaintService service) {
        this.service = service;
    }

    @GetMapping("/notifications")
    public List<Notification> getAll() {
        return service.getNotifications();
    }

    @PostMapping("/notifications/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markRead(@RequestBody ReadRequest r) {
        service.markRead(r);
    }

    @PostMapping("/demo/reset")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reset() {
        service.resetAll();
    }
}
