package pl.yshop.plugin.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.yshop.plugin.shared.commands.PlatformCommand;
import pl.yshop.plugin.shared.commands.PlatformCommandManager;

import java.lang.reflect.Field;

public class BukkitCommandManager implements PlatformCommandManager {
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
    public void registerCommand(PlatformCommand command) {
        this.commandMap.register(command.getName(), new Command(command.getName()) {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
                command.execute(new BukkitSender(commandSender), strings);
                return false;
            }
        });
    }
}
