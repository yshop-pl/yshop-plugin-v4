package pl.yshop.plugin.bukkit.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.yshop.plugin.shared.commands.PlatformSender;

import java.util.UUID;

public class BukkitSender implements PlatformSender {
    private final CommandSender sender;

    public BukkitSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(String message) {
        this.sender.sendMessage(message);
    }

    @Override
    public boolean hasPermissions(String permission) {
        return this.sender.hasPermission(permission);
    }

    @Override
    public String getName() {
        return this.sender.getName();
    }

    @Override
    public UUID getUniqueId() {
        if (this.sender instanceof Player player) {
            return player.getUniqueId();
        }
        return null;
    }
}
