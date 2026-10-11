package com.prajaconnect.service;

import java.util.Map;

/** Same departments / users as src/data/masterData.ts (used only to build readable notes). */
public final class MasterData {
    private static final Map<String, String> DEPARTMENTS = Map.of(
            "d1", "Roads & Buildings",
            "d2", "Sanitation & Solid Waste",
            "d3", "Rural Water Supply",
            "d4", "Drainage & Sewerage",
            "d5", "Street Lighting (Electrical)",
            "d6", "Public Works & Maintenance");

    private static final Map<String, String> USERS = Map.ofEntries(
            Map.entry("c1", "Lakshmi Reddy"),
            Map.entry("c2", "Venkat Rao"),
            Map.entry("c3", "Ayesha Begum"),
            Map.entry("a1", "K. Srinivas (Grievance Officer)"),
            Map.entry("s1", "Ramesh Goud"),
            Map.entry("s2", "Naveen Kumar"),
            Map.entry("s3", "Sunitha Yadav"),
            Map.entry("s4", "Prakash Naik"),
            Map.entry("s5", "Mahesh Chary"),
            Map.entry("s6", "Anitha Sharma"),
            Map.entry("s7", "Rajesh Varma"));

    private MasterData() {}

    public static String deptName(String id) {
        return DEPARTMENTS.getOrDefault(id, "—");
    }

    public static String userName(String id) {
        return USERS.getOrDefault(id, "—");
    }
}
