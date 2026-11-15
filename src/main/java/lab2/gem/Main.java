package lab2.gem;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lab2.gem.logging.LoggingConfig;
import lab2.gem.model.GemType;

public final class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final Path DEFAULT_OUTPUT = Path.of("target", "grouped-gems.xml");

    private Main() {
    }

    public static void main(String[] args) {
        LoggingConfig.apply();
        try {
            byte[] xml = readXml(args);
            Path output = args.length > 1 ? Path.of(args[1]) : DEFAULT_OUTPUT;

            List<GemType> gems = new GemApplication().run(xml, output);
            LOGGER.info(() -> "Application finished, gems in collection: " + gems.size());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Application failed", e);
        }
    }

    private static byte[] readXml(String[] args) throws IOException {
        if (args.length > 0) {
            return Files.readAllBytes(Path.of(args[0]));
        }
        try (InputStream stream = Main.class.getResourceAsStream(GemApplication.DEFAULT_XML_RESOURCE)) {
            if (stream == null) {
                throw new IllegalStateException("Default XML resource not found");
            }
            return stream.readAllBytes();
        }
    }
}
