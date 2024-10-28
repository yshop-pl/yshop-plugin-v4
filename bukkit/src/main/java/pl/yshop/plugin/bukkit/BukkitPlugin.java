package pl.yshop.plugin.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.yshop.plugin.api.Configuration;
import pl.yshop.plugin.bukkit.commands.BukkitCommandManager;
import pl.yshop.plugin.bukkit.impl.BukkitConfiguration;
import pl.yshop.plugin.bukkit.impl.BukkitLogger;
import pl.yshop.plugin.bukkit.impl.BukkitTask;
import pl.yshop.plugin.shared.Bootstrap;
import pl.yshop.plugin.api.Platform;

public class BukkitPlugin extends JavaPlugin implements Platform {
    private final Bootstrap bootstrap = new Bootstrap(this)
            .withConfiguration(new BukkitConfiguration(this));

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.bootstrap
                .withLogger(new BukkitLogger(this.getLogger()))
                .withCommandManager(new BukkitCommandManager())
                .enableExtensions(this.getDataFolder())
                .start();

        this.getServer().getScheduler().runTaskTimerAsynchronously(
                this,
                new BukkitTask(this.bootstrap.request, this),
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
    public Configuration getConfiguration() {
        return this.bootstrap.configuration;
    }
}