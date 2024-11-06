package pl.yshop.plugin.bungee;

import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import pl.yshop.plugin.api.Platform;
import pl.yshop.plugin.bungee.commands.BungeeCommandManager;
import pl.yshop.plugin.shared.Bootstrap;
import pl.yshop.plugin.shared.PlatformCommandManager;
import pl.yshop.plugin.shared.tasks.ExecuteCommandsTask;

import java.util.concurrent.TimeUnit;

public class BungeePlugin extends Plugin {
    public static BungeeAudiences audiences;
    private Bootstrap bootstrap;

    @Override
    public void onEnable() {
        audiences = BungeeAudiences.create(this);
        PlatformCommandManager<CommandSender> commandManager = new BungeeCommandManager(this);
        Platform platform = new BungeePlatform(this, commandManager);
        this.bootstrap = new Bootstrap(platform)
                .withCommandManager(commandManager)
                .enableExtensions(this.getDataFolder())
                .start();

        this.getProxy().getScheduler().schedule(
                this,
                new ExecuteCommandsTask(platform),
                0L,
                platform.getConfiguration().taskInterval().getSeconds(),
                TimeUnit.SECONDS
        );
    }

    @Override
    public void onDisable() {
        this.getProxy().getScheduler().cancel(this);
        this.bootstrap.start();
    }
}
