package pl.yshop.plugin.shared.entities;

import java.util.List;

public class Command {
    private String nickname;
    private String id;
    private String commands;
    private boolean require_online;
    private List<String> command_list;

    public String getId() {
        return id;
    }

    public String getCommands() {
        return commands;
    }

    public boolean requireOnline() {
        return require_online;
    }

    public List<String> getCommandList() {
        return command_list;
    }

    public String getNickname() {
        return nickname;
    }
}
