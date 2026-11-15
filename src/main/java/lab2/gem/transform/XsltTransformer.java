package lab2.gem.transform;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.XMLConstants;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XsltTransformer {

    private final Templates templates;

    public XsltTransformer(InputStream xsl) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        this.templates = factory.newTemplates(new StreamSource(xsl));
    }

    public static XsltTransformer fromResource(String resource) throws TransformerException {
        try (InputStream stream = open(resource)) {
            return new XsltTransformer(stream);
        } catch (IOException e) {
            throw new TransformerException("Failed to read stylesheet " + resource, e);
        }
    }

    public void transform(InputStream xml, OutputStream output) throws TransformerException {
        templates.newTransformer().transform(new StreamSource(xml), new StreamResult(output));
    }

    private static InputStream open(String resource) {
        InputStream stream = XsltTransformer.class.getResourceAsStream(resource);
        if (stream == null) {
            throw new IllegalArgumentException("Resource not found: " + resource);
        }
        return stream;
    }
}
