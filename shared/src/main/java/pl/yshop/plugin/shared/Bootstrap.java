package pl.yshop.plugin.shared;

import pl.yshop.plugin.shared.configuration.ConfigProperties;
import pl.yshop.plugin.shared.configuration.PluginConfiguration;
import pl.yshop.plugin.shared.logger.YShopLogger;
import pl.yshop.plugin.shared.platform.Platform;
import pl.yshop.plugin.shared.request.YShopRequest;

import java.io.File;

public class Bootstrap {
    private YShopLogger logger;
    public PluginConfiguration configuration;
    public YShopRequest request;
    public Platform platform;
    public ExtensionsLoader extensionsLoader;

    public Bootstrap withLogger(YShopLogger logger) {
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
        this.extensionsLoader = new ExtensionsLoader(dataFolder, this.logger, this.configuration);
        return this;
    }

    public Bootstrap start() {
        this.extensionsLoader.load();
        this.extensionsLoader.enable();
        return this;
    }

    public void stop() {
        this.request.shutdown();
        this.extensionsLoader.disable();
    }
}
