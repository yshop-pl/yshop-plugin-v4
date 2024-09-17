package pl.yshop.plugin.shared.commands;

public abstract class PlatformCommand {
    public abstract String getName();
    public abstract void execute(PlatformSender sender, String[] args);
}
