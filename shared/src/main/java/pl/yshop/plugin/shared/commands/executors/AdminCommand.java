package pl.yshop.plugin.shared.commands.executors;

import pl.yshop.plugin.shared.Bootstrap;
import pl.yshop.plugin.shared.commands.PlatformCommand;
import pl.yshop.plugin.shared.commands.PlatformSender;

public class AdminCommand extends PlatformCommand {
    private final Bootstrap bootstrap;

    public AdminCommand(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    public String getName() {
        return "ya";
    }

    @Override
    public void execute(PlatformSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Invalid usage: /yshop <extensions>");
            return;
        }
        if (args[0].equalsIgnoreCase("extensions")) {
            sender.sendMessage("Loaded extensions:");
            this.bootstrap.getExtensionsLoader().getExtensions().forEach(it -> {
                sender.sendMessage("- " + it.getExtensionName());
            });
        }
    }
}
