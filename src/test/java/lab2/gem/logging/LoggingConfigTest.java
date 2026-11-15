package lab2.gem.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

class LoggingConfigTest {

    @Test
    void loadsConfigurationFromClasspath() {
        LoggingConfig.apply();

        assertEquals(Level.INFO, Logger.getLogger("lab2.gem").getLevel());
        assertTrue(Logger.getLogger("").getHandlers().length > 0);
    }
}
