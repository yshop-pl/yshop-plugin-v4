package pl.yshop.plugin.api;

import pl.yshop.plugin.api.commands.PlatformCommand;

public abstract class Extension {
    private PlatformLogger logger;
    public Platform platform;

    public void init(PlatformLogger logger, Platform platform) {
        this.logger = logger;
        this.platform = platform;
    }

    public abstract void onEnable();
    public abstract void onDisable();

    public void registerCommand(PlatformCommand command) {

    }
    public PlatformLogger getLogger() {
        return this.logger;
    }
    public final String getExtensionName() {
        return getClass().getSimpleName();
    }
}
