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
    private PlatformLogger logger;
    public PluginConfiguration configuration;
    public YShopRequest request;
    public Platform platform;
    public ExtensionsLoader extensionsLoader;
    public PlatformCommandManager commandManager;

    public Bootstrap withLogger(PlatformLogger logger) {
        this.logger = logger;
        return this;
    }
    public Bootstrap withConfiguration(ConfigProperties properties) {
        this.configuration = new PluginConfiguration(properties);
        return this;
    }
    public Bootstrap withPlatform(Platform platform) {
        this.platform = platform;
        return this;
    }
    public Bootstrap withRequests() {
        this.request = new YShopRequest(this.configuration, this.platform, this.logger);
        return this;
    }
    public Bootstrap enableExtensions(File dataFolder) {
        this.extensionsLoader = new ExtensionsLoader(dataFolder, this.logger, this.configuration, this);
        return this;
    }

    public Bootstrap withCommandManager(PlatformCommandManager platformCommandManager) {
        this.commandManager = platformCommandManager;
        return this;
    }

    public Bootstrap start() {
        this.extensionsLoader.load();
        this.extensionsLoader.enable();

        if (this.commandManager != null) {
            this.commandManager.registerCommand(new AdminCommand(this));
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
