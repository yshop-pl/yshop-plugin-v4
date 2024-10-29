package pl.yshop.plugin.velocity.configuration;

import ninja.leaping.configurate.ConfigurationNode;
import pl.yshop.plugin.shared.configuration.ConfigProperties;

public class VelocityConfiguration implements ConfigProperties {
    private final ConfigurationNode root;
    public VelocityConfiguration(ConfigurationNode root) {
        this.root = root;
    }

    @Override
    public String getString(String key) {
        return this.root.getString(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return Boolean.getBoolean(this.root.getString(key));
    }
}
