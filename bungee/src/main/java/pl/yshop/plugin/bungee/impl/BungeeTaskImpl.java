package pl.yshop.plugin.bungee.impl;

import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.plugin.Plugin;
import pl.yshop.plugin.shared.request.YShopRequest;
import pl.yshop.plugin.shared.tasks.ExecuteCommandsTask;

public class BungeeTaskImpl extends ExecuteCommandsTask {
    private final Plugin plugin;
    private BungeeAudiences audiences;

    public BungeeTaskImpl(YShopRequest requester, Plugin plugin) {
        super(requester);
        this.plugin = plugin;
        this.audiences = BungeeAudiences.create(plugin);;
    }

    @Override
    public boolean isPlayerOnline(String nickname) {
        return this.plugin.getProxy().getPlayer(nickname) != null;
    }

    @Override
    public void executeCommand(String command) {
        this.plugin.getProxy().getPluginManager().dispatchCommand(this.plugin.getProxy().getConsole(), command);    }

    @Override
    public void announce(String message) {
        this.plugin.getProxy().getPlayers().forEach(player -> {
            this.audiences.player(player).sendMessage(MiniMessage.miniMessage().deserialize(message));
        });
    }
}
