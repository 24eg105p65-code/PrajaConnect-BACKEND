package com.prajaconnect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AdminNote {
    @Column(name = "note_text", length = 1500)
    public String text;
    @Column(name = "by_name")
    public String by;
    @Column(name = "at_time")
    public String at;

    public AdminNote() {}

    public AdminNote(String text, String by, String at) {
        this.text = text;
        this.by = by;
        this.at = at;
    }
}
