package lab2.gem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;
import lab2.gem.model.GemType;
import lab2.gem.parser.DomGemParser;
import lab2.gem.parser.GemParser;
import lab2.gem.parser.GemParsingException;
import lab2.gem.parser.SaxGemParser;
import lab2.gem.parser.StaxGemParser;
import lab2.gem.sort.GemComparator;
import lab2.gem.transform.XsltTransformer;
import lab2.gem.validation.XsdValidator;
import org.xml.sax.SAXException;

public class GemApplication {

    public static final String DEFAULT_XML_RESOURCE = "/xml/gems.xml";

    private static final Logger LOGGER = Logger.getLogger(GemApplication.class.getName());

    private final GemParser saxParser;
    private final GemParser domParser;
    private final GemParser staxParser;
    private final XsdValidator validator;
    private final XsltTransformer transformer;

    public GemApplication() throws SAXException, TransformerException {
        this(
                new SaxGemParser(),
                new DomGemParser(),
                new StaxGemParser(),
                XsdValidator.fromResource("/xsd/gems.xsd"),
                XsltTransformer.fromResource("/xsl/group-by-preciousness.xsl"));
    }

    GemApplication(GemParser saxParser, GemParser domParser, GemParser staxParser,
                   XsdValidator validator, XsltTransformer transformer) {
        this.saxParser = saxParser;
        this.domParser = domParser;
        this.staxParser = staxParser;
        this.validator = validator;
        this.transformer = transformer;
    }

    public List<GemType> run(byte[] xml, Path outputFile)
            throws GemParsingException, TransformerException, IOException {
        if (!validator.isValid(new ByteArrayInputStream(xml))) {
            throw new GemParsingException("XML document is not valid against XSD", null);
        }
        LOGGER.info("XML document passed XSD validation");

        List<GemType> gems = saxParser.parse(new ByteArrayInputStream(xml));
        LOGGER.info(() -> "SAX parser loaded " + gems.size() + " gems");

        int domCount = domParser.parse(new ByteArrayInputStream(xml)).size();
        LOGGER.info(() -> "DOM parser loaded " + domCount + " gems");

        int staxCount = staxParser.parse(new ByteArrayInputStream(xml)).size();
        LOGGER.info(() -> "StAX parser loaded " + staxCount + " gems");

        gems.sort(new GemComparator());
        LOGGER.info(() -> "Gems sorted by preciousness, weight and name");

        Path absoluteOutput = outputFile.toAbsolutePath();
        if (absoluteOutput.getParent() != null) {
            Files.createDirectories(absoluteOutput.getParent());
        }
        try (OutputStream output = Files.newOutputStream(absoluteOutput)) {
            transformer.transform(new ByteArrayInputStream(xml), output);
        }
        LOGGER.info(() -> "Grouped XML saved to " + absoluteOutput);
        return gems;
    }
}
