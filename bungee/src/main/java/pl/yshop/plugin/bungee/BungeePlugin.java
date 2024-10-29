package pl.yshop.plugin.bungee;

import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.plugin.Plugin;
import pl.yshop.plugin.api.Configuration;
import pl.yshop.plugin.api.PlatformLogger;
import pl.yshop.plugin.api.request.Requester;
import pl.yshop.plugin.bungee.impl.BungeeConfigurationImpl;
import pl.yshop.plugin.bungee.impl.BungeeLoggerImpl;
import pl.yshop.plugin.shared.configuration.ConfigProperties;
import pl.yshop.plugin.api.Platform;
import pl.yshop.plugin.shared.request.YShopRequest;
import pl.yshop.plugin.shared.tasks.ExecuteCommandsTask;

import java.util.concurrent.TimeUnit;

public class BungeePlugin extends Plugin implements Platform {
    private YShopRequest request;
    private Configuration configuration;
    private final BungeeAudiences audiences = BungeeAudiences.create(this);
    private final PlatformLogger logger = new BungeeLoggerImpl(this.getLogger());

    @Override
    public void onEnable() {
        BungeeConfigurationManager configManager = new BungeeConfigurationManager(this);
        configManager.loadConfigurationFile();

        ConfigProperties properties = new BungeeConfigurationImpl(configManager.getConfiguration());
        this.configuration = new Configuration(
                properties.getString("serverId"),
                properties.getString("serverKey"),
                properties.getString("apiKey"),
                properties.getString("apiUrl"),
                properties.getBoolean("debug")
        );
        this.request = new YShopRequest(configuration, this, logger);

        this.getProxy().getScheduler().schedule(
                this,
                new ExecuteCommandsTask(this),
                0L,
                configuration.taskInterval().getSeconds(),
                TimeUnit.SECONDS
        );
    }

    @Override
    public void onDisable() {
        this.getProxy().getScheduler().cancel(this);
        this.request.shutdown();
    }

    @Override
    public String version() {
        return this.getDescription().getVersion();
    }

    @Override
    public String engine() {
        return "BungeeCord";
    }

    @Override
    public boolean isPluginEnabled(String name) {
        return this.getProxy().getPluginManager().getPlugin(name) != null;
    }

    @Override
    public boolean isPlayerOnline(String nickname) {
        return this.getProxy().getPlayer(nickname) != null;
    }

    @Override
    public void executeCommand(String command) {
        this.getProxy().getPluginManager().dispatchCommand(this.getProxy().getConsole(), command);
    }

    @Override
    public void announce(String message) {
        this.getProxy().getPlayers().forEach(player -> {
            this.audiences.player(player).sendMessage(MiniMessage.miniMessage().deserialize(message));
        });
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }

    @Override
    public Requester getRequester() {
        return this.request;
    }

    @Override
    public PlatformLogger logger() {
        return this.logger;
    }
}
