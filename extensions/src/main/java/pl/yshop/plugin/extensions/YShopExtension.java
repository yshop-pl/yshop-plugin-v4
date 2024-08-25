package pl.yshop.plugin.extensions;

public abstract class YShopExtension {
    public abstract void onEnable();

    public abstract void onDisable();

    public final String getExtensionName() {
        return getClass().getSimpleName();
    }
}
