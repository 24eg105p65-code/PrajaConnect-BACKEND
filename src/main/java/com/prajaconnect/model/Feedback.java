package com.prajaconnect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Feedback {
    public Integer rating;
    @Column(name = "feedback_comment", length = 1500)
    public String comment;
    @Column(name = "feedback_at")
    public String at;

    public Feedback() {}

    public Feedback(Integer rating, String comment, String at) {
        this.rating = rating;
        this.comment = comment;
        this.at = at;
    }
}
