package pl.yshop.plugin.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import pl.yshop.plugin.bukkit.commands.BukkitCommandManager;
import pl.yshop.plugin.shared.Bootstrap;
import pl.yshop.plugin.api.Platform;
import pl.yshop.plugin.shared.PlatformCommandManager;
import pl.yshop.plugin.shared.commands.AdminCommand;
import pl.yshop.plugin.shared.tasks.ExecuteCommandsTask;

public class BukkitPlugin extends JavaPlugin {
    private Bootstrap bootstrap;
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        PlatformCommandManager<CommandSender> commandManager = new BukkitCommandManager();
        Platform platform = new BukkitPlatform(this, commandManager);

        this.bootstrap = new Bootstrap(platform)
                .withCommandManager(commandManager)
                .enableExtensions(this.getDataFolder())
                .start();

        this.getServer().getScheduler().runTaskTimerAsynchronously(
                this,
                new ExecuteCommandsTask(platform),
                0L,
                platform.getConfiguration().taskInterval().getSeconds() * 20L
        );
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        this.bootstrap.stop();
    }
}