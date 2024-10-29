package pl.yshop.plugin.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ninja.leaping.configurate.ConfigurationNode;
import org.slf4j.Logger;
import pl.yshop.plugin.api.Configuration;
import pl.yshop.plugin.api.Platform;
import pl.yshop.plugin.api.PlatformLogger;
import pl.yshop.plugin.api.request.Requester;
import pl.yshop.plugin.shared.configuration.ConfigProperties;
import pl.yshop.plugin.shared.request.YShopRequest;
import pl.yshop.plugin.velocity.configuration.VelocityConfiguration;

public class VelocityPlatform implements Platform {
    private final ProxyServer server;
    private final ConfigurationNode configurationNode;
    private final Logger logger;
    public VelocityPlatform(final ProxyServer server, Logger logger, ConfigurationNode configurationNode) {
        this.server = server;
        this.configurationNode = configurationNode;
        this.logger = logger;
    }

    @Override
    public String version() {
        return "";
    }

    @Override
    public String engine() {
        return "";
    }

    @Override
    public boolean isPluginEnabled(String name) {
        return this.server.getPluginManager().isLoaded(name);
    }

    @Override
    public boolean isPlayerOnline(String nickname) {
        return this.server.getPlayer(nickname).isPresent();
    }

    @Override
    public void executeCommand(String command) {
        this.server.getCommandManager().executeAsync(this.server.getConsoleCommandSource(), command);
    }

    @Override
    public void announce(String message) {
        this.server.getAllPlayers().forEach(player -> {
            player.sendMessage(MiniMessage.miniMessage().deserialize(message));
        });
    }

    @Override
    public Configuration getConfiguration() {
        ConfigProperties properties = new VelocityConfiguration(this.configurationNode);
        return new Configuration(
                properties.getString("serverId"),
                properties.getString("serverKey"),
                properties.getString("apiKey"),
                properties.getString("apiUrl"),
                properties.getBoolean("debug")
        );
    }

    @Override
    public Requester getRequester() {
        return new YShopRequest(this);
    }

    @Override
    public PlatformLogger logger() {
        return new VelocityLogger(this.logger);
    }
}
