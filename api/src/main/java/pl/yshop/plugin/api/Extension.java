package pl.yshop.plugin.api;

import pl.yshop.plugin.commands.PlatformCommand;
import pl.yshop.plugin.commands.PlatformCommandManager;

public abstract class Extension {
    private PlatformLogger logger;
    private PlatformCommandManager commandManager;

    public void init(PlatformLogger logger, PlatformCommandManager commandManager) {
        this.logger = logger;
        this.commandManager = commandManager;
    }

    public abstract void onEnable();
    public abstract void onDisable();

    public void registerCommand(PlatformCommand command) {
        this.commandManager.register(command);
    }
    public PlatformLogger getLogger() {
        return this.logger;
    }
    public final String getExtensionName() {
        return getClass().getSimpleName();
    }
}
