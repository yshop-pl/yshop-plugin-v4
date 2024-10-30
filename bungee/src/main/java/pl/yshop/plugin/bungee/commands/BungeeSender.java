package pl.yshop.plugin.bungee.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pl.yshop.plugin.bungee.BungeePlugin;
import pl.yshop.plugin.api.commands.PlatformSender;

import java.util.UUID;

public class BungeeSender implements PlatformSender {
    private final CommandSender sender;

    public BungeeSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(String message) {
        BungeePlugin.audiences.sender(this.sender).sendMessage(MiniMessage.miniMessage().deserialize(message));
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
        if (this.sender instanceof ProxiedPlayer player) {
            return player.getUniqueId();
        }
        return null;
    }
}
