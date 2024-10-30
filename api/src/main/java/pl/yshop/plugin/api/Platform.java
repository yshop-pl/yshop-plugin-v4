package pl.yshop.plugin.api;

import pl.yshop.plugin.api.commands.PlatformCommand;
import pl.yshop.plugin.api.request.Requester;

public interface Platform {
    String version();
    String engine();

    boolean isPluginEnabled(String name);
    boolean isPlayerOnline(String nickname);
    void executeCommand(String command);
    void announce(String message);

    void registerCommand(PlatformCommand command);

    Configuration getConfiguration();
    Requester getRequester();

    PlatformLogger logger();
}
