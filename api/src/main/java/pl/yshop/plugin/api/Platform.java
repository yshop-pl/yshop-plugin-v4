package pl.yshop.plugin.api;

public interface Platform {
    String version();
    String engine();

    boolean isPluginEnabled(String name);

    Configuration getConfiguration();
}
