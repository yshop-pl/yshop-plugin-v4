package pl.yshop.plugin.bukkit.impl;

import org.bukkit.plugin.Plugin;
import pl.yshop.plugin.shared.configuration.ConfigProperties;

public class BukkitConfigurationImpl implements ConfigProperties {
    private final Plugin plugin;

    public BukkitConfigurationImpl(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getString(String key) {
        return this.plugin.getConfig().getString(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return this.plugin.getConfig().getBoolean(key);
    }
}
