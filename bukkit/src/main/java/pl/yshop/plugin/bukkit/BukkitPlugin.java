package pl.yshop.plugin.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.yshop.plugin.api.Configuration;
import pl.yshop.plugin.api.PlatformLogger;
import pl.yshop.plugin.api.request.Requester;
import pl.yshop.plugin.bukkit.commands.BukkitCommandManager;
import pl.yshop.plugin.bukkit.impl.BukkitConfiguration;
import pl.yshop.plugin.bukkit.impl.BukkitLogger;
import pl.yshop.plugin.bukkit.impl.BukkitTask;
import pl.yshop.plugin.shared.Bootstrap;
import pl.yshop.plugin.api.Platform;
import pl.yshop.plugin.shared.tasks.ExecuteCommandsTask;

public class BukkitPlugin extends JavaPlugin implements Platform {
    private final PlatformLogger logger = new BukkitLogger(this.getLogger());
    private final Bootstrap bootstrap = new Bootstrap(this)
            .withConfiguration(new BukkitConfiguration(this))
            .withLogger(this.logger);

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.bootstrap
                .withCommandManager(new BukkitCommandManager())
                .enableExtensions(this.getDataFolder())
                .start();

        //new BukkitTask(this.bootstrap.request, this),
        this.getServer().getScheduler().runTaskTimerAsynchronously(
                this,
                new ExecuteCommandsTask(this),
                0L,
                this.bootstrap.configuration.taskInterval().getSeconds() * 20L
        );
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        this.bootstrap.stop();
    }

    @Override
    public String version() {
        return this.getDescription().getVersion();
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
        return this.getServer().getPlayer(nickname) != null;
    }

    @Override
    public void executeCommand(String command) {
        this.getServer().getScheduler().runTask(this, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }

    @Override
    public void announce(String message) {
        this.getServer().getOnlinePlayers().forEach(player -> {
            player.sendMessage(message);
        });
    }

    @Override
    public Configuration getConfiguration() {
        return this.bootstrap.configuration;
    }

    @Override
    public Requester getRequester() {
        return this.bootstrap.request;
    }

    @Override
    public PlatformLogger logger() {
        return this.logger;
    }
}