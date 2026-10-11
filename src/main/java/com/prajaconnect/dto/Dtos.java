package com.prajaconnect.dto;

import java.util.List;

/** Request bodies. "actor" is the logged-in user ({id, name, role}) sent by the frontend. */
public class Dtos {
    public record Actor(String id, String name, String role) {}

    public record CreateRequest(String title, String description, String category,
                                String village, String mandal, String district,
                                String imageName, String citizenId, String citizenName) {}

    public record StatusRequest(String status, String note, Actor actor) {}

    public record AssignRequest(String departmentId, String staffId, String note, Actor actor) {}

    public record NoteRequest(String text, Actor actor) {}

    public record ProgressRequest(String status, String note, List<String> evidence, Actor actor) {}

    public record ReplyRequest(String text, Actor actor) {}

    public record FeedbackRequest(int rating, String comment) {}

    public record ReadRequest(String userId, String id) {}
}
