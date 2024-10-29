package pl.yshop.plugin.bungee;

import pl.yshop.plugin.api.PlatformLogger;
import java.util.logging.Logger;

public class BungeeLogger implements PlatformLogger {
    private final Logger logger;

    public BungeeLogger(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        this.logger.info(message);
    }

    @Override
    public void error(String message) {
        this.logger.severe(message);
    }

    @Override
    public void debug(String message) {
        this.logger.info(String.format("[DEBUG] %s", message));
    }

    @Override
    public void warn(String message) {
        this.logger.warning(message);
    }
}

