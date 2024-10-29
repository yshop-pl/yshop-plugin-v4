package pl.yshop.plugin.velocity;

import org.slf4j.Logger;
import pl.yshop.plugin.api.PlatformLogger;


public class VelocityLogger implements PlatformLogger {
    private final Logger logger;

    public VelocityLogger(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        this.logger.info(message);
    }

    @Override
    public void error(String message) {
        this.logger.error(message);
    }

    @Override
    public void debug(String message) {
        this.logger.debug(message);
    }

    @Override
    public void warn(String message) {
        this.logger.warn(message);
    }
}
