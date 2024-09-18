package pl.yshop.plugin.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.yshop.plugin.commands.PlatformCommand;
import pl.yshop.plugin.commands.PlatformCommandManager;
import pl.yshop.plugin.commands.annotations.Execute;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

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
        pl.yshop.plugin.commands.annotations.Command annotation = command.getClass().getAnnotation(pl.yshop.plugin.commands.annotations.Command.class);
        if (annotation != null) {
            this.commandMap.register(annotation.name(), new Command(annotation.name()) {
                @Override
                public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
                    Method method = Arrays.stream(command.getClass().getDeclaredMethods()).filter(it -> {
                        Execute execute = it.getDeclaredAnnotation(Execute.class);
                        if (strings.length <= 0) return true;
                        return execute.name().equals(strings[0]);
                    }).findFirst().orElse(null);
                    if (method != null) {
                        method.setAccessible(true);
                        try {
                            method.invoke(command, new BukkitSender(commandSender));
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return false;
                }
            });
        }
    }
}
