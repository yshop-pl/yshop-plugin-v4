package pl.yshop.plugin.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import pl.yshop.plugin.api.commands.PlatformCommand;
import pl.yshop.plugin.shared.PlatformCommandManager;
import pl.yshop.plugin.api.commands.PlatformSender;

public class BungeeCommandManager extends PlatformCommandManager<CommandSender> {
    private final Plugin plugin;
    public BungeeCommandManager(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public PlatformSender sender(CommandSender commandSender) {
        return new BungeeSender(commandSender);
    }

    @Override
    public void register(PlatformCommand command) {
        String name = this.command(command).name();
        ProxyServer.getInstance().getPluginManager().registerCommand(this.plugin, new Command(name) {
            @Override
            public void execute(CommandSender commandSender, String[] strings) {
                registerCommand(commandSender, command, strings);
            }
        });
    }
}
