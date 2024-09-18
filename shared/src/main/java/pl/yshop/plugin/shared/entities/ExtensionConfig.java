package pl.yshop.plugin.shared.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExtensionConfig {
    @SerializedName("main_class")
    private String mainClass;

    private String version;
    private String author;
    private String description;
    private String name;

    @SerializedName("supported_platforms")
    private List<String> supportedPlatforms;

    public String getMainClass() {
        return mainClass;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getSupportedPlatforms() {
        return supportedPlatforms;
    }

    public String getName() {
        return name;
    }
}
