package com.prajaconnect.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.*;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize(using = ToStringSerializer.class)   // frontend expects id as a string
    public Long id;

    public String userId;
    @Column(name = "message", length = 1000)
    public String text;
    public String complaintId;
    @Column(name = "at_time")
    public String at;
    @Column(name = "is_read")
    public boolean read;

    public Notification() {}

    public Notification(String userId, String text, String complaintId, String at) {
        this.userId = userId;
        this.text = text;
        this.complaintId = complaintId;
        this.at = at;
        this.read = false;
    }
}
