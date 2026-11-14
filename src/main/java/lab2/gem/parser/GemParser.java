package lab2.gem.parser;

import java.io.InputStream;
import java.util.List;
import lab2.gem.model.GemType;

public interface GemParser {

    List<GemType> parse(InputStream xml) throws GemParsingException;
}
