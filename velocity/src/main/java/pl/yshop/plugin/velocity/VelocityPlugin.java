package pl.yshop.plugin.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import ninja.leaping.configurate.ConfigurationNode;
import org.slf4j.Logger;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import pl.yshop.plugin.api.Platform;
import pl.yshop.plugin.shared.Bootstrap;
import pl.yshop.plugin.shared.tasks.ExecuteCommandsTask;
import pl.yshop.plugin.velocity.configuration.ConfigLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Plugin(
        id = "yshop-plugin",
        name = "yShopPlugin",
        version = "1.2",
        url = "https://yshop.pl",
        description = "Plugin for communication with yshop.pl api",
        authors = {"yShop Contributors"}
)
public class VelocityPlugin {
    @Inject private @DataDirectory Path dataDirectory;
    @Inject private ProxyServer proxy;
    @Inject private Logger logger;
    private Bootstrap bootstrap;
    private ScheduledTask executionTask;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        ConfigurationNode node = this.getConfiguration();
        Platform platform = new VelocityPlatform(this.proxy, this.logger, node);

        this.bootstrap = new Bootstrap(platform)
                .enableExtensions(this.dataDirectory.toFile())
                .start();

        this.executionTask = this.proxy.getScheduler()
                .buildTask(this, new ExecuteCommandsTask(platform))
                .repeat(platform.getConfiguration().taskInterval())
                .schedule();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        if (this.executionTask != null) {
            this.executionTask.cancel();
        }
        this.bootstrap.stop();
    }

    private ConfigurationNode getConfiguration() {
        File pluginDataFolder = this.dataDirectory.toFile();
        if (!pluginDataFolder.exists() && pluginDataFolder.mkdirs()) {
            this.logger.info("Successfully created data folder.");
        }
        ConfigLoader configLoader = new ConfigLoader(this.getClass(), pluginDataFolder.toPath());
        configLoader.copyConfig("config.yml");
        try {
            return YAMLConfigurationLoader.builder().setPath(Path.of(pluginDataFolder.getPath(), "config.yml")).build().load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
