package com.prajaconnect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class HistoryEntry {
    public String status;
    @Column(length = 1500)
    public String note;
    @Column(name = "by_name")
    public String by;
    public String role;
    @Column(name = "at_time")
    public String at;

    public HistoryEntry() {}

    public HistoryEntry(String status, String note, String by, String role, String at) {
        this.status = status;
        this.note = note;
        this.by = by;
        this.role = role;
        this.at = at;
    }
}
