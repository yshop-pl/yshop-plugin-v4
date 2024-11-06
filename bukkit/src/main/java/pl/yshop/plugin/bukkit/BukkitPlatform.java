package pl.yshop.plugin.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import pl.yshop.plugin.api.Configuration;
import pl.yshop.plugin.api.Platform;
import pl.yshop.plugin.api.PlatformLogger;
import pl.yshop.plugin.api.commands.PlatformCommand;
import pl.yshop.plugin.api.request.Requester;
import pl.yshop.plugin.shared.PlatformCommandManager;
import pl.yshop.plugin.shared.configuration.ConfigProperties;
import pl.yshop.plugin.shared.request.YShopRequest;

public class BukkitPlatform implements Platform {
    private final Plugin plugin;
    private final PlatformCommandManager<CommandSender> commandManager;

    public BukkitPlatform(final Plugin plugin, PlatformCommandManager<CommandSender> commandManager) {
        this.plugin = plugin;
        this.commandManager = commandManager;
    }

    @Override
    public String version() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public String engine() {
        return "Bukkit";
    }

    @Override
    public boolean isPluginEnabled(String name) {
        return Bukkit.getPluginManager().isPluginEnabled(name);
    }

    @Override
    public boolean isPlayerOnline(String nickname) {
        return this.plugin.getServer().getPlayer(nickname) != null;
    }

    @Override
    public void executeCommand(String command) {
        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }

    @Override
    public void announce(String message) {
        this.plugin.getServer().getOnlinePlayers().forEach(player -> {
            player.sendMessage(message);
        });
    }

    @Override
    public void registerCommand(PlatformCommand command) {
        this.commandManager.register(command);
    }

    @Override
    public Configuration getConfiguration() {
        ConfigProperties properties = new BukkitConfiguration(this.plugin);
        return new Configuration(
                properties.getString("serverId"),
                properties.getString("serverKey"),
                properties.getString("apiKey"),
                properties.getString("apiUrl"),
                properties.getBoolean("debug")
        );
    }

    @Override
    public PlatformLogger logger() {
        return new BukkitLogger(this.plugin.getLogger());
    }

    @Override
    public Requester getRequester() {
        return new YShopRequest(this);
    }

    @Override
    public Object plugin() {
        return this.plugin;
    }
}
