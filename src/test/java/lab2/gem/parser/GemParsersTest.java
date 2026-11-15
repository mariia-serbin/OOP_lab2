package lab2.gem.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;
import lab2.gem.TestResources;
import lab2.gem.model.ColorType;
import lab2.gem.model.GemType;
import lab2.gem.model.PreciousnessType;
import lab2.gem.model.VisualParametersType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GemParsersTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("parsers")
    void parsesAllGemFields(String name, GemParser parser) throws Exception {
        List<GemType> gems = parser.parse(TestResources.stream("/xml/gems.xml"));

        assertEquals(8, gems.size());

        GemType diamond = gems.get(0);
        assertEquals("g1", diamond.getId());
        assertEquals("Diamond", diamond.getName());
        assertEquals(PreciousnessType.PRECIOUS, diamond.getPreciousness());
        assertEquals("South Africa", diamond.getOrigin());
        assertEquals(ColorType.COLORLESS, diamond.getVisualParameters().getColor());
        assertEquals(98, diamond.getVisualParameters().getTransparency());
        assertEquals(12, diamond.getVisualParameters().getFacetCount());
        assertEquals(new BigDecimal("1.25"), diamond.getValue());

        GemType moonstone = gems.get(7);
        assertEquals("Moonstone", moonstone.getName());
        assertEquals(PreciousnessType.SEMIPRECIOUS, moonstone.getPreciousness());
    }

    @Test
    void allParsersReturnSameData() throws Exception {
        List<String> sax = signatures(new SaxGemParser().parse(TestResources.stream("/xml/gems.xml")));
        List<String> dom = signatures(new DomGemParser().parse(TestResources.stream("/xml/gems.xml")));
        List<String> stax = signatures(new StaxGemParser().parse(TestResources.stream("/xml/gems.xml")));

        assertEquals(sax, dom);
        assertEquals(sax, stax);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("parsers")
    void reportsMalformedXml(String name, GemParser parser) {
        ByteArrayInputStream broken = new ByteArrayInputStream(
                "<Gem><gem>".getBytes(StandardCharsets.UTF_8));

        assertThrows(GemParsingException.class, () -> parser.parse(broken));
    }

    @Test
    void rejectsIncompleteGem() {
        GemBuilder builder = new GemBuilder();
        builder.reset("g99");

        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<Arguments> parsers() {
        return Stream.of(
                Arguments.of("SAX", new SaxGemParser()),
                Arguments.of("DOM", new DomGemParser()),
                Arguments.of("StAX", new StaxGemParser()));
    }

    private static List<String> signatures(List<GemType> gems) {
        return gems.stream().map(GemParsersTest::signature).toList();
    }

    private static String signature(GemType gem) {
        VisualParametersType visual = gem.getVisualParameters();
        return String.join("|",
                gem.getId(),
                gem.getName(),
                gem.getPreciousness().value(),
                gem.getOrigin(),
                visual.getColor().value(),
                Integer.toString(visual.getTransparency()),
                Integer.toString(visual.getFacetCount()),
                gem.getValue().toPlainString());
    }
}
