package pl.yshop.plugin.bungee.impl;

import net.md_5.bungee.config.Configuration;
import pl.yshop.plugin.shared.configuration.ConfigProperties;

public class BungeeConfigurationImpl implements ConfigProperties {
    private final Configuration configuration;

    public BungeeConfigurationImpl(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getString(String key) {
        return this.configuration.getString(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return this.configuration.getBoolean(key);
    }
}
