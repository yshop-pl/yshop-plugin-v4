package pl.yshop.plugin.shared.tasks;

import pl.yshop.plugin.api.Platform;
import pl.yshop.plugin.shared.entities.Command;
import pl.yshop.plugin.shared.request.requests.CommandsToExecuteRequest;
import pl.yshop.plugin.api.request.RequestException;
import pl.yshop.plugin.shared.request.requests.ConfirmCommandExecutionRequest;

import java.util.Arrays;

public class ExecuteCommandsTask implements Runnable {
    private final Platform platform;

    public ExecuteCommandsTask(Platform platform) {
        this.platform = platform;
    }

    @Override
    public void run() {
        try {
            Command[] commands = this.platform.getRequester().make(new CommandsToExecuteRequest());
            Arrays.stream(commands).toList().forEach(this::processCommands);
        } catch (RequestException e) {
            this.platform.logger().error("Wystapil blad podczas pobierania listy komend do wykonania!");
            this.platform.logger().error("Blad: " + e.getMessage());
        }
    }

    private void processCommands(Command command) {
        if (command.requireOnline() && !this.platform.isPlayerOnline(command.getNickname())) return;
        try {
            this.platform.getRequester().make(new ConfirmCommandExecutionRequest(command.getId()));
            command.getCommandList().forEach(cmd -> {
                if (cmd.startsWith("announce")) {
                    this.platform.announce(cmd.replaceFirst("announce",""));
                    return;
                }
                this.platform.executeCommand(cmd);
            });
        } catch (RequestException e) {
            this.platform.logger().error("Nie mozna potwierdzic zamowienia " + command.getId());
            this.platform.logger().error("Blad: " + e.getMessage());
        }
    }
}
