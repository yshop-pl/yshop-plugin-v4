package pl.yshop.plugin.api.example.commands;

import pl.yshop.plugin.commands.PlatformCommand;
import pl.yshop.plugin.commands.PlatformSender;

public class ExampleCommand extends PlatformCommand {

    @Override
    public String getName() {
        return "example";
    }

    @Override
    public void execute(PlatformSender sender, String[] args) {
        sender.sendMessage("Example command");
    }
}
