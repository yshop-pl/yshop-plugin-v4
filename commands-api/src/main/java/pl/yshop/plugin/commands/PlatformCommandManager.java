package pl.yshop.plugin.commands;

import pl.yshop.plugin.commands.annotations.Command;
import pl.yshop.plugin.commands.annotations.Execute;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public abstract class PlatformCommandManager<SENDER> {
    public abstract PlatformSender sender(SENDER sender);
    public abstract void register(PlatformCommand command);

    public Command command(PlatformCommand command) {
        return command.getClass().getAnnotation(Command.class);
    }

    public boolean registerCommand(SENDER sender, PlatformCommand command, String[] args) {
        Method method = Arrays.stream(command.getClass().getDeclaredMethods()).filter(it -> {
            Execute execute = it.getDeclaredAnnotation(Execute.class);
            if (args.length <= 0) return true;
            return execute.name().equals(args[0]);
        }).findFirst().orElse(null);
        if (method != null) {
            method.setAccessible(true);
            try {
                method.invoke(command, this.sender(sender));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
