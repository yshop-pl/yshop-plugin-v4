package pl.yshop.plugin.shared.tasks;

import pl.yshop.plugin.shared.entities.Command;
import pl.yshop.plugin.shared.request.requests.CommandsToExecuteRequest;
import pl.yshop.plugin.shared.request.RequestException;
import pl.yshop.plugin.shared.request.YShopRequest;
import pl.yshop.plugin.shared.request.requests.ConfirmCommandExecutionRequest;

import java.util.Arrays;

public abstract class ExecuteCommandsTask implements Runnable {
    public abstract boolean isPlayerOnline(String nickname);
    public abstract void executeCommand(String command);
    public abstract void announce(String message);

    private final YShopRequest requester;

    public ExecuteCommandsTask(YShopRequest requester) {
        this.requester = requester;
    }

    @Override
    public void run() {
        try {
            Command[] commands = this.requester.make(new CommandsToExecuteRequest());
            Arrays.stream(commands).toList().forEach(this::processCommands);
        } catch (RequestException e) {
            this.requester.logger.error("Wystapil blad podczas pobierania listy komend do wykonania!");
            this.requester.logger.error("Blad: " + e.getMessage());
        }
    }

    private void processCommands(Command command) {
        if (command.requireOnline() && !this.isPlayerOnline(command.getNickname())) return;
        try {
            this.requester.make(new ConfirmCommandExecutionRequest(command.getId()));
            command.getCommandList().forEach(cmd -> {
                if (cmd.startsWith("announce")) {
                    this.announce(cmd.replaceFirst("announce",""));
                    return;
                }
                this.executeCommand(cmd);
            });
        } catch (RequestException e) {
            this.requester.logger.error("Nie mozna potwierdzic zamowienia " + command.getId());
            this.requester.logger.error("Blad: " + e.getMessage());
        }
    }
}
