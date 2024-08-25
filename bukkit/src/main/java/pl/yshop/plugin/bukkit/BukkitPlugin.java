package pl.yshop.plugin.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.yshop.plugin.bukkit.impl.BukkitConfigurationImpl;
import pl.yshop.plugin.bukkit.impl.BukkitLoggerImpl;
import pl.yshop.plugin.bukkit.impl.BukkitTaskImpl;
import pl.yshop.plugin.extensions.ExtensionsLoader;
import pl.yshop.plugin.shared.configuration.PluginConfiguration;
import pl.yshop.plugin.shared.logger.YShopLogger;
import pl.yshop.plugin.shared.platform.Platform;
import pl.yshop.plugin.shared.request.YShopRequest;

public class BukkitPlugin extends JavaPlugin implements Platform {
    private YShopRequest request;
    private ExtensionsLoader extensionsLoader;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        PluginConfiguration configuration = new PluginConfiguration(new BukkitConfigurationImpl(this));
        YShopLogger logger = new BukkitLoggerImpl(this.getLogger());
        this.request = new YShopRequest(configuration, this, logger);

        this.getServer().getScheduler().runTaskTimerAsynchronously(
                this,
                new BukkitTaskImpl(this.request, this),
                0L,
                configuration.taskInterval.getSeconds() * 20L
        );

        this.extensionsLoader = new ExtensionsLoader(this.getDataFolder(), logger, configuration);
        this.extensionsLoader.load();
        this.extensionsLoader.enable();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        this.request.shutdown();
        this.extensionsLoader.disable();
    }

    @Override
    public String version() {
        return this.getDescription().getVersion();
    }

    @Override
    public String engine() {
        return "Bukkit";
    }
}