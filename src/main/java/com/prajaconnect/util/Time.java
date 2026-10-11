package com.prajaconnect.util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/** ISO timestamps with fixed width, same shape as JavaScript's toISOString(). */
public final class Time {
    private static final DateTimeFormatter F =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC);

    private Time() {}

    public static String now() {
        return F.format(Instant.now());
    }
}
