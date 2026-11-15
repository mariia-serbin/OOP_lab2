package lab2.gem.validation;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

public class XsdValidator {

    private static final Logger LOGGER = Logger.getLogger(XsdValidator.class.getName());

    private final Schema schema;

    private XsdValidator(Schema schema) {
        this.schema = schema;
    }

    public static XsdValidator fromResource(String resource) throws SAXException {
        try (InputStream stream = open(resource)) {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            return new XsdValidator(factory.newSchema(new StreamSource(stream)));
        } catch (IOException e) {
            throw new SAXException("Failed to read schema " + resource, e);
        }
    }

    public boolean isValid(InputStream xml) {
        try {
            Validator validator = schema.newValidator();
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            validator.validate(new StreamSource(xml));
            return true;
        } catch (SAXException | IOException e) {
            LOGGER.log(Level.WARNING, "XML validation failed: {0}", e.getMessage());
            return false;
        }
    }

    private static InputStream open(String resource) {
        InputStream stream = XsdValidator.class.getResourceAsStream(resource);
        if (stream == null) {
            throw new IllegalArgumentException("Resource not found: " + resource);
        }
        return stream;
    }
}
