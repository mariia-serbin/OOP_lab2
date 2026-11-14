package lab2.gem.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import lab2.gem.model.GemType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxGemParser implements GemParser {

    @Override
    public List<GemType> parse(InputStream xml) throws GemParsingException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            SAXParser parser = factory.newSAXParser();
            GemHandler handler = new GemHandler();
            parser.parse(xml, handler);
            return handler.getGems();
        } catch (ParserConfigurationException | SAXException | IOException | RuntimeException e) {
            throw new GemParsingException("Failed to parse gem XML with SAX", e);
        }
    }

    private static final class GemHandler extends DefaultHandler {

        private final List<GemType> gems = new ArrayList<>();
        private final GemBuilder builder = new GemBuilder();
        private final StringBuilder text = new StringBuilder();
        private boolean insideGem;

        List<GemType> getGems() {
            return gems;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            String element = elementName(localName, qName);
            text.setLength(0);

            if (XmlNames.GEM.equals(element)) {
                insideGem = true;
                builder.reset(attributes.getValue(XmlNames.ID_ATTRIBUTE));
            }
        }

        @Override
        public void characters(char[] chars, int start, int length) {
            if (insideGem) {
                text.append(chars, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if (!insideGem) {
                return;
            }

            String element = elementName(localName, qName);
            switch (element) {
                case XmlNames.NAME -> builder.setName(text.toString());
                case XmlNames.PRECIOUSNESS -> builder.setPreciousness(text.toString());
                case XmlNames.ORIGIN -> builder.setOrigin(text.toString());
                case XmlNames.COLOR -> builder.setColor(text.toString());
                case XmlNames.TRANSPARENCY -> builder.setTransparency(text.toString());
                case XmlNames.FACET_COUNT -> builder.setFacetCount(text.toString());
                case XmlNames.VALUE -> builder.setValue(text.toString());
                case XmlNames.GEM -> {
                    gems.add(builder.build());
                    insideGem = false;
                }
                default -> {
                }
            }
            text.setLength(0);
        }

        private static String elementName(String localName, String qName) {
            return localName == null || localName.isEmpty() ? qName : localName;
        }
    }
}
