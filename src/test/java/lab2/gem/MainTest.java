package lab2.gem;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MainTest {

    @Test
    void readsXmlFromFileAndWritesOutput(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("gems.xml");
        Path output = tempDir.resolve("out/grouped-gems.xml");
        Files.write(input, TestResources.bytes("/xml/gems.xml"));

        Main.main(new String[]{input.toString(), output.toString()});

        assertTrue(Files.exists(output));
        assertTrue(Files.readString(output).contains("<GemGroups>"));
    }

    @Test
    void usesDefaultXmlResourceAndDefaultOutput() throws Exception {
        Path defaultOutput = Path.of("target", "grouped-gems.xml");
        Files.deleteIfExists(defaultOutput);

        Main.main(new String[0]);

        assertTrue(Files.exists(defaultOutput));
    }
}
