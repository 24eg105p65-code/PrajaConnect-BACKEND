package com.prajaconnect.service;

import com.prajaconnect.dto.Dtos.*;
import com.prajaconnect.model.*;
import com.prajaconnect.repository.ComplaintRepository;
import com.prajaconnect.repository.NotificationRepository;
import com.prajaconnect.util.Time;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Year;
import java.util.List;
import java.util.Set;

/** Same business rules as the old localStorage complaintApi.ts. */
@Service
public class ComplaintService {

    private static final Set<String> STATUSES = Set.of(
            "SUBMITTED", "UNDER_REVIEW", "ASSIGNED", "IN_PROGRESS",
            "RESOLUTION_SUBMITTED", "RESOLVED", "REJECTED", "NEEDS_INFORMATION");

    private final ComplaintRepository complaints;
    private final NotificationRepository notifications;

    public ComplaintService(ComplaintRepository complaints, NotificationRepository notifications) {
        this.complaints = complaints;
        this.notifications = notifications;
    }

    // ---------- helpers ----------
    private static boolean blank(String s) { return s == null || s.isBlank(); }

    private static String label(String s) { return s.replace('_', ' ').toLowerCase(); }

    private static Actor actor(Actor a) {
        return a == null ? new Actor("", "System", "SYSTEM") : a;
    }

    private Complaint find(String id) {
        return complaints.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Complaint not found."));
    }

    private void checkStatus(String status) {
        if (status == null || !STATUSES.contains(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status: " + status);
        }
    }

    private void notify(String userId, String text, String complaintId) {
        if (blank(userId)) return;
        notifications.save(new Notification(userId, text, complaintId, Time.now()));
    }

    private void push(Complaint c, String status, String note, Actor a) {
        checkStatus(status);
        c.status = status;
        c.updatedAt = Time.now();
        c.history.add(new HistoryEntry(status, note, a.name(), a.role(), c.updatedAt));
    }

    private static int seq(String id) {
        try { return Integer.parseInt(id.split("-")[2]); } catch (Exception e) { return 0; }
    }

    // ---------- queries ----------
    @Transactional(readOnly = true)
    public List<Complaint> getAll() {
        return complaints.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    // ---------- commands ----------
    @Transactional
    public synchronized Complaint create(CreateRequest r) {
        if (blank(r.title()) || blank(r.description()) || blank(r.category())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title, description and category are required.");
        }
        int next = complaints.findAllIds().stream().mapToInt(ComplaintService::seq).max().orElse(0) + 1;
        String id = "PC-" + Year.now().getValue() + "-" + String.format("%04d", next);
        String t = Time.now();

        Complaint c = new Complaint();
        c.id = id;
        c.title = r.title();
        c.description = r.description();
        c.category = r.category();
        c.village = r.village();
        c.mandal = r.mandal();
        c.district = r.district();
        c.imageName = r.imageName();
        c.citizenId = r.citizenId();
        c.citizenName = r.citizenName();
        c.status = "SUBMITTED";
        c.createdAt = t;
        c.updatedAt = t;
        c.history.add(new HistoryEntry("SUBMITTED", "Complaint submitted by citizen.", r.citizenName(), "CITIZEN", t));
        complaints.save(c);

        notify("a1", "New complaint " + id + " submitted in " + c.mandal + ", " + c.district + ".", id);
        return c;
    }

    @Transactional
    public Complaint updateStatus(String id, StatusRequest r) {
        Complaint c = find(id);
        Actor a = actor(r.actor());
        push(c, r.status(), blank(r.note()) ? "Status changed to " + label(r.status()) + "." : r.note(), a);
        notify(c.citizenId, id + " is now " + label(r.status()) + ".", id);
        if (!blank(c.staffId) && "ADMIN".equals(a.role())) {
            notify(c.staffId, id + " status changed to " + label(r.status()) + ".", id);
        }
        return complaints.save(c);
    }

    @Transactional
    public Complaint assign(String id, AssignRequest r) {
        Complaint c = find(id);
        c.departmentId = r.departmentId();
        c.staffId = r.staffId();
        String dept = MasterData.deptName(r.departmentId());
        push(c, "ASSIGNED",
                "Assigned to " + dept + " (" + MasterData.userName(r.staffId()) + ")."
                        + (blank(r.note()) ? "" : " " + r.note()),
                actor(r.actor()));
        notify(c.citizenId, id + " was assigned to " + dept + ".", id);
        notify(r.staffId(), "You were assigned " + id + ": " + c.title + ".", id);
        return complaints.save(c);
    }

    @Transactional
    public Complaint addAdminNote(String id, NoteRequest r) {
        Complaint c = find(id);
        c.adminNotes.add(new AdminNote(r.text(), actor(r.actor()).name(), Time.now()));
        c.updatedAt = Time.now();
        return complaints.save(c);
    }

    @Transactional
    public Complaint submitProgress(String id, ProgressRequest r) {
        Complaint c = find(id);
        Actor a = actor(r.actor());
        if (r.evidence() != null) c.evidence.addAll(r.evidence());
        push(c, r.status(), r.note(), a);
        notify(c.citizenId, id + ": work update. Status is now " + label(r.status()) + ".", id);
        if ("RESOLUTION_SUBMITTED".equals(r.status())) {
            notify("a1", id + ": resolution submitted by " + a.name() + ". Verify and close it.", id);
        }
        return complaints.save(c);
    }

    @Transactional
    public Complaint citizenReply(String id, ReplyRequest r) {
        Complaint c = find(id);
        push(c, "UNDER_REVIEW", "Citizen provided information: " + r.text(), actor(r.actor()));
        notify("a1", id + ": citizen replied with the requested information.", id);
        return complaints.save(c);
    }

    @Transactional
    public Complaint submitFeedback(String id, FeedbackRequest r) {
        Complaint c = find(id);
        c.feedback = new Feedback(r.rating(), r.comment(), Time.now());
        notify("a1", "Feedback received on " + id + ": " + r.rating() + "/5.", id);
        return complaints.save(c);
    }

    // ---------- notifications ----------
    @Transactional(readOnly = true)
    public List<Notification> getNotifications() {
        return notifications.findAllByOrderByIdDesc();
    }

    @Transactional
    public void markRead(ReadRequest r) {
        List<Notification> list = notifications.findByUserId(r.userId());
        for (Notification n : list) {
            if (blank(r.id()) || String.valueOf(n.id).equals(r.id())) n.read = true;
        }
        notifications.saveAll(list);
    }

    @Transactional
    public void resetAll() {
        complaints.deleteAll();
        notifications.deleteAll();
    }
}
