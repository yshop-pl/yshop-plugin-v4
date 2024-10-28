package pl.yshop.plugin.api;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public record Configuration(String serverId, String serverKey, String apiKey, String apiUrl, boolean debug) {
    public Duration taskInterval() {
        return Duration.of(30, ChronoUnit.SECONDS);
    }
}