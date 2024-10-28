package pl.yshop.plugin.shared.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class Command {
    private String id;
    private String commands;
    @SerializedName("require_online")
    private boolean requireOnline;
    private String created_at;
    private String nickname;

    public String getId() {
        return id;
    }

    public boolean requireOnline() {
        return this.requireOnline;
    }

    public List<String> getCommandList() {
        byte[] decodedBytes = Base64.getDecoder().decode(this.commands);
        String decodedString = new String(decodedBytes);
        return Arrays.stream(decodedString.split("\\|\\|")).toList();
    }

    public String getNickname() {
        return nickname;
    }
}
