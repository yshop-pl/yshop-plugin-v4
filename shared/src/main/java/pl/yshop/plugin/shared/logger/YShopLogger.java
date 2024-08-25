package pl.yshop.plugin.shared.logger;

public interface YShopLogger {
    void info(String message);
    void error(String message);
    void debug(String message);
    void warn(String message);
}
