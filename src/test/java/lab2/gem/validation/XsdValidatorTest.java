package lab2.gem.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import lab2.gem.TestResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

class XsdValidatorTest {

    private XsdValidator validator;

    @BeforeEach
    void setUp() throws SAXException {
        validator = XsdValidator.fromResource("/xsd/gems.xsd");
    }

    @Test
    void acceptsValidXml() {
        assertTrue(validator.isValid(TestResources.stream("/xml/gems.xml")));
    }

    @Test
    void rejectsXmlWithValuesOutsideSchemaLimits() {
        assertFalse(validator.isValid(TestResources.stream("/xml/invalid-gems.xml")));
    }

    @Test
    void rejectsMalformedXml() {
        ByteArrayInputStream broken = new ByteArrayInputStream(
                "<Gem>".getBytes(StandardCharsets.UTF_8));

        assertFalse(validator.isValid(broken));
    }

    @Test
    void failsWhenSchemaResourceIsMissing() {
        assertThrows(IllegalArgumentException.class,
                () -> XsdValidator.fromResource("/xsd/missing.xsd"));
    }
}
