package pl.yshop.plugin.bukkit.impl;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import pl.yshop.plugin.shared.request.YShopRequest;
import pl.yshop.plugin.shared.tasks.ExecuteCommandsTask;

public class BukkitTask extends ExecuteCommandsTask {
    private final Plugin plugin;

    public BukkitTask(YShopRequest requester, Plugin plugin) {
        super(requester);
        this.plugin = plugin;
    }

    @Override
    public boolean isPlayerOnline(String nickname) {
        return this.plugin.getServer().getPlayer(nickname) != null;
    }

    @Override
    public void executeCommand(String command) {
        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }

    @Override
    public void announce(String message) {
        this.plugin.getServer().getOnlinePlayers().forEach(player -> {
            player.sendMessage(message);
        });
    }
}
