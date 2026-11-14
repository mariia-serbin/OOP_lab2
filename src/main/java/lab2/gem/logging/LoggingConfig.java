package lab2.gem.logging;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public final class LoggingConfig {

    private static final Logger LOGGER = Logger.getLogger(LoggingConfig.class.getName());

    private LoggingConfig() {
    }

    public static void apply() {
        try (InputStream stream = LoggingConfig.class.getResourceAsStream("/logging.properties")) {
            if (stream == null) {
                LOGGER.warning("logging.properties not found; using default JUL settings");
                return;
            }
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to load logging.properties", e);
        }
    }
}
