package lab2.gem.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import lab2.gem.model.GemType;

public class StaxGemParser implements GemParser {

    @Override
    public List<GemType> parse(InputStream xml) throws GemParsingException {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);

            List<GemType> gems = new ArrayList<>();
            GemBuilder builder = new GemBuilder();
            StringBuilder text = new StringBuilder();
            boolean insideGem = false;

            XMLStreamReader reader = factory.createXMLStreamReader(xml);
            try {
                while (reader.hasNext()) {
                    int event = reader.next();
                    switch (event) {
                        case XMLStreamConstants.START_ELEMENT -> {
                            text.setLength(0);
                            if (XmlNames.GEM.equals(reader.getLocalName())) {
                                insideGem = true;
                                builder.reset(reader.getAttributeValue(null, XmlNames.ID_ATTRIBUTE));
                            }
                        }
                        case XMLStreamConstants.CHARACTERS, XMLStreamConstants.CDATA -> {
                            if (insideGem) {
                                text.append(reader.getText());
                            }
                        }
                        case XMLStreamConstants.END_ELEMENT -> {
                            if (insideGem) {
                                readElement(reader.getLocalName(), text.toString(), builder, gems);
                                if (XmlNames.GEM.equals(reader.getLocalName())) {
                                    insideGem = false;
                                }
                            }
                            text.setLength(0);
                        }
                        default -> {
                        }
                    }
                }
            } finally {
                reader.close();
            }
            return gems;
        } catch (XMLStreamException | RuntimeException e) {
            throw new GemParsingException("Failed to parse gem XML with StAX", e);
        }
    }

    private static void readElement(String element, String text, GemBuilder builder, List<GemType> gems) {
        switch (element) {
            case XmlNames.NAME -> builder.setName(text);
            case XmlNames.PRECIOUSNESS -> builder.setPreciousness(text);
            case XmlNames.ORIGIN -> builder.setOrigin(text);
            case XmlNames.COLOR -> builder.setColor(text);
            case XmlNames.TRANSPARENCY -> builder.setTransparency(text);
            case XmlNames.FACET_COUNT -> builder.setFacetCount(text);
            case XmlNames.VALUE -> builder.setValue(text);
            case XmlNames.GEM -> gems.add(builder.build());
            default -> {
            }
        }
    }
}
