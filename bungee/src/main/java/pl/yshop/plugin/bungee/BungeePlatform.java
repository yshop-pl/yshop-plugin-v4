package pl.yshop.plugin.bungee;

import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.plugin.Plugin;
import pl.yshop.plugin.api.Configuration;
import pl.yshop.plugin.api.Platform;
import pl.yshop.plugin.api.PlatformLogger;
import pl.yshop.plugin.api.request.Requester;
import pl.yshop.plugin.bungee.configuration.BungeeConfigurationManager;
import pl.yshop.plugin.bungee.configuration.BungeeConfiguration;
import pl.yshop.plugin.shared.configuration.ConfigProperties;
import pl.yshop.plugin.shared.request.YShopRequest;

public class BungeePlatform implements Platform {
    private final Plugin plugin;
    private final BungeeAudiences audiences;

    public BungeePlatform(Plugin plugin) {
        this.plugin = plugin;
        this.audiences = BungeeAudiences.create(plugin);
    }

    @Override
    public String version() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public String engine() {
        return "BungeeCord";
    }

    @Override
    public boolean isPluginEnabled(String name) {
        return this.plugin.getProxy().getPluginManager().getPlugin(name) != null;
    }

    @Override
    public boolean isPlayerOnline(String nickname) {
        return this.plugin.getProxy().getPlayer(nickname) != null;
    }

    @Override
    public void executeCommand(String command) {
        this.plugin.getProxy().getPluginManager().dispatchCommand(this.plugin.getProxy().getConsole(), command);
    }

    @Override
    public void announce(String message) {
        this.plugin.getProxy().getPlayers().forEach(player -> {
            this.audiences.player(player).sendMessage(MiniMessage.miniMessage().deserialize(message));
        });
    }

    @Override
    public Configuration getConfiguration() {
        BungeeConfigurationManager configManager = new BungeeConfigurationManager(this.plugin);
        configManager.loadConfigurationFile();
        ConfigProperties properties = new BungeeConfiguration(configManager.getConfiguration());
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
        return new BungeeLogger(this.plugin.getLogger());
    }
}
