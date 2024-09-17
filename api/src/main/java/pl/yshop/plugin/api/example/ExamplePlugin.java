package pl.yshop.plugin.api.example;

import pl.yshop.plugin.api.Extension;
import pl.yshop.plugin.api.example.commands.ExampleCommand;

public class ExamplePlugin extends Extension {
    @Override
    public void onEnable() {
        this.registerCommand(new ExampleCommand());
        this.getLogger().info("Loaded Example Plugin!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Unloaded Example Plugin!");
    }
}
