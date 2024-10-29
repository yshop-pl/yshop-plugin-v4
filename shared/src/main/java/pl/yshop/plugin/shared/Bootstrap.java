package pl.yshop.plugin.shared;

import pl.yshop.plugin.commands.PlatformCommandManager;
import pl.yshop.plugin.shared.commands.AdminCommand;
import pl.yshop.plugin.api.Platform;
import pl.yshop.plugin.shared.request.YShopRequest;

import java.io.File;

public class Bootstrap {
    private final Platform platform;
    public YShopRequest request;
    public ExtensionsLoader extensionsLoader;
    public PlatformCommandManager<?> commandManager;

    public Bootstrap(Platform platform) {
        this.platform = platform;
    }

    public Bootstrap enableExtensions(File dataFolder) {
        this.extensionsLoader = new ExtensionsLoader(dataFolder, this.platform);
        return this;
    }

    public Bootstrap withCommandManager(PlatformCommandManager<?> platformCommandManager) {
        this.commandManager = platformCommandManager;
        return this;
    }

    public Bootstrap start() {
        this.request = new YShopRequest(this.platform);

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
