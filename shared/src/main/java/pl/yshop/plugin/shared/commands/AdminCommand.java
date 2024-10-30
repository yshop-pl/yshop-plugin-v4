package pl.yshop.plugin.shared.commands;

import pl.yshop.plugin.api.commands.PlatformCommand;
import pl.yshop.plugin.api.commands.PlatformSender;
import pl.yshop.plugin.api.commands.annotations.Command;
import pl.yshop.plugin.api.commands.annotations.Execute;
import pl.yshop.plugin.shared.Bootstrap;

@Command(name = "yshop")
public class AdminCommand implements PlatformCommand {
    private final Bootstrap bootstrap;

    public AdminCommand(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Execute()
    void execute(PlatformSender sender) {
        sender.sendMessage("/yshop <extensions>");
    }

    @Execute(name = "extensions")
    void extensions(PlatformSender sender) {
        sender.sendMessage("Loaded extensions:");
        this.bootstrap.getExtensionsLoader().extensions.forEach(it -> {
            sender.sendMessage("- " + it.getExtensionName());
        });
    }
}
