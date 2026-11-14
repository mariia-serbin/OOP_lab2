package lab2.gem.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lab2.gem.model.GemType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DomGemParser implements GemParser {

    @Override
    public List<GemType> parse(InputStream xml) throws GemParsingException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xml);
            NodeList nodes = document.getElementsByTagName(XmlNames.GEM);

            List<GemType> gems = new ArrayList<>(nodes.getLength());
            for (int i = 0; i < nodes.getLength(); i++) {
                gems.add(readGem((Element) nodes.item(i)));
            }
            return gems;
        } catch (ParserConfigurationException | SAXException | IOException | RuntimeException e) {
            throw new GemParsingException("Failed to parse gem XML with DOM", e);
        }
    }

    private static GemType readGem(Element element) {
        GemBuilder builder = new GemBuilder();
        builder.reset(element.getAttribute(XmlNames.ID_ATTRIBUTE));
        builder.setName(text(element, XmlNames.NAME));
        builder.setPreciousness(text(element, XmlNames.PRECIOUSNESS));
        builder.setOrigin(text(element, XmlNames.ORIGIN));

        Element visualParameters = child(element, XmlNames.VISUAL_PARAMETERS);
        builder.setColor(text(visualParameters, XmlNames.COLOR));
        builder.setTransparency(text(visualParameters, XmlNames.TRANSPARENCY));
        builder.setFacetCount(text(visualParameters, XmlNames.FACET_COUNT));

        builder.setValue(text(element, XmlNames.VALUE));
        return builder.build();
    }

    private static String text(Element parent, String childName) {
        return child(parent, childName).getTextContent();
    }

    private static Element child(Element parent, String childName) {
        NodeList children = parent.getElementsByTagName(childName);
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getParentNode() == parent) {
                return (Element) node;
            }
        }
        throw new IllegalArgumentException("Missing <" + childName + "> element");
    }
}
