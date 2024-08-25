package pl.yshop.plugin.shared.configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class PluginConfiguration {
    public final String serverId;
    public final String serverKey;
    public final String apiKey;
    public final String apiUrl;
    public final boolean debug;


    public final Duration taskInterval = Duration.of(30, ChronoUnit.SECONDS);

    public PluginConfiguration(ConfigProperties properties) {
        this.serverId = properties.getString("serverId");
        this.serverKey = properties.getString("serverKey");
        this.apiKey = properties.getString("apiKey");
        this.apiUrl = properties.getString("apiUrl");
        this.debug = properties.getBoolean("debug");
    }
}
