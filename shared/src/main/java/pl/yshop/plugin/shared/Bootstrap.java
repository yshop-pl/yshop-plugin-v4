package pl.yshop.plugin.shared;

import pl.yshop.plugin.api.PlatformLogger;
import pl.yshop.plugin.commands.PlatformCommandManager;
import pl.yshop.plugin.shared.commands.AdminCommand;
import pl.yshop.plugin.shared.configuration.ConfigProperties;
import pl.yshop.plugin.shared.configuration.PluginConfiguration;
import pl.yshop.plugin.shared.platform.Platform;
import pl.yshop.plugin.shared.request.YShopRequest;

import java.io.File;

public class Bootstrap {
    private final Platform platform;
    private PlatformLogger logger;
    public PluginConfiguration configuration;
    public YShopRequest request;
    public ExtensionsLoader extensionsLoader;
    public PlatformCommandManager<?> commandManager;

    public Bootstrap(Platform platform) {
        this.platform = platform;
    }

    public Bootstrap withLogger(PlatformLogger logger) {
        this.logger = logger;
        return this;
    }
    public Bootstrap withConfiguration(ConfigProperties properties) {
        this.configuration = new PluginConfiguration(properties);
        return this;
    }
    public Bootstrap enableExtensions(File dataFolder) {
        this.extensionsLoader = new ExtensionsLoader(dataFolder, this.platform, this.logger, this.configuration, this);
        return this;
    }

    public Bootstrap withCommandManager(PlatformCommandManager<?> platformCommandManager) {
        this.commandManager = platformCommandManager;
        return this;
    }

    public Bootstrap start() {
        this.request = new YShopRequest(this.configuration, this.platform, this.logger);

        this.extensionsLoader.load();
        this.extensionsLoader.enable();

        if (this.commandManager != null) {
            this.commandManager.register(new AdminCommand(this));
        }

        return this;
    }

    public void stop() {
        this.request.shutdown();
        this.extensionsLoader.disable();
    }

    public ExtensionsLoader getExtensionsLoader() {
        return this.extensionsLoader;
    }
}
