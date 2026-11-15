package lab2.gem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lab2.gem.model.GemType;
import lab2.gem.parser.GemParsingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GemApplicationTest {

    @Test
    void runsWholePipeline(@TempDir Path tempDir) throws Exception {
        Path output = tempDir.resolve("nested/grouped-gems.xml");

        List<GemType> gems = new GemApplication().run(TestResources.bytes("/xml/gems.xml"), output);

        assertEquals(8, gems.size());
        assertEquals("Sapphire", gems.get(0).getName());
        assertEquals("Moonstone", gems.get(7).getName());
        assertTrue(Files.exists(output));
        assertTrue(Files.readString(output).contains("<GemGroups>"));
    }

    @Test
    void rejectsXmlThatDoesNotMatchSchema(@TempDir Path tempDir) throws Exception {
        Path output = tempDir.resolve("grouped-gems.xml");
        GemApplication application = new GemApplication();

        assertThrows(GemParsingException.class,
                () -> application.run(TestResources.bytes("/xml/invalid-gems.xml"), output));
        assertFalse(Files.exists(output));
    }
}
