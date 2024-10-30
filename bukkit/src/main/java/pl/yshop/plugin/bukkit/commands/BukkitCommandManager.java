package pl.yshop.plugin.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.yshop.plugin.api.commands.PlatformCommand;
import pl.yshop.plugin.shared.PlatformCommandManager;
import pl.yshop.plugin.api.commands.PlatformSender;

import java.lang.reflect.Field;

public class BukkitCommandManager extends PlatformCommandManager<CommandSender> {
    private CommandMap commandMap;

    public BukkitCommandManager() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            this.commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public PlatformSender sender(CommandSender commandSender) {
        return new BukkitSender(commandSender);
    }

    @Override
    public void register(PlatformCommand command) {
        String name = this.command(command).name();
        this.commandMap.register(name, new Command(name) {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
                return registerCommand(commandSender, command, strings);
            }
        });
    }
}
