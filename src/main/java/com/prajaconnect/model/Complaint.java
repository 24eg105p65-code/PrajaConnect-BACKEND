package com.prajaconnect.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/** Field names match src/types/index.ts (Complaint) exactly. */
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Complaint {

    @Id
    public String id;                       // e.g. PC-2026-0001

    public String title;
    @Column(length = 2000)
    public String description;
    public String category;
    public String village;
    public String mandal;
    public String district;
    public String imageName;

    public String citizenId;
    public String citizenName;
    public String status;
    public String departmentId;
    public String staffId;
    public String createdAt;
    public String updatedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "complaint_history", joinColumns = @JoinColumn(name = "complaint_id"))
    @OrderColumn(name = "idx")
    public List<HistoryEntry> history = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "complaint_admin_notes", joinColumns = @JoinColumn(name = "complaint_id"))
    @OrderColumn(name = "idx")
    public List<AdminNote> adminNotes = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "complaint_evidence", joinColumns = @JoinColumn(name = "complaint_id"))
    @OrderColumn(name = "idx")
    @Column(name = "evidence_name")
    public List<String> evidence = new ArrayList<>();

    @Embedded
    public Feedback feedback;
}
