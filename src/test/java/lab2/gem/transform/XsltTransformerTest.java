package lab2.gem.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import lab2.gem.TestResources;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

class XsltTransformerTest {

    @Test
    void groupsGemsByPreciousness() throws Exception {
        XsltTransformer transformer = XsltTransformer.fromResource("/xsl/group-by-preciousness.xsl");
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        transformer.transform(TestResources.stream("/xml/gems.xml"), output);

        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new ByteArrayInputStream(output.toByteArray()));

        Element root = document.getDocumentElement();
        assertEquals("GemGroups", root.getTagName());

        NodeList groups = root.getElementsByTagName("Group");
        assertEquals(2, groups.getLength());

        Element precious = (Element) groups.item(0);
        assertEquals("precious", precious.getAttribute("preciousness"));
        assertEquals("4", precious.getAttribute("count"));
        assertEquals(4, precious.getElementsByTagName("gem").getLength());

        Element semiprecious = (Element) groups.item(1);
        assertEquals("semiprecious", semiprecious.getAttribute("preciousness"));
        assertEquals("4", semiprecious.getAttribute("count"));
        assertEquals(4, semiprecious.getElementsByTagName("gem").getLength());
    }

    @Test
    void failsWhenStylesheetResourceIsMissing() {
        assertThrows(IllegalArgumentException.class,
                () -> XsltTransformer.fromResource("/xsl/missing.xsl"));
    }
}
