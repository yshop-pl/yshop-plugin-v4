package pl.yshop.plugin.api;

public abstract class Extension {
    public abstract void onEnable();

    public abstract void onDisable();

    public final String getExtensionName() {
        return getClass().getSimpleName();
    }
}
