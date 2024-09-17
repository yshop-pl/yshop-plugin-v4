package pl.yshop.plugin.commands;

import java.util.UUID;

public interface PlatformSender {
    void sendMessage(String message);
    boolean hasPermissions(String permission);
    String getName();
    UUID getUniqueId();
}