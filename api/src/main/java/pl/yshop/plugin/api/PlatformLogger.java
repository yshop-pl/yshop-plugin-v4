package pl.yshop.plugin.api;

public interface PlatformLogger {
    void info(String message);
    void error(String message);
    void debug(String message);
    void warn(String message);
}
