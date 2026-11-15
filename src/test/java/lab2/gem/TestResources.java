package lab2.gem;

import java.io.IOException;
import java.io.InputStream;

public final class TestResources {

    private TestResources() {
    }

    public static InputStream stream(String name) {
        InputStream stream = TestResources.class.getResourceAsStream(name);
        if (stream == null) {
            throw new IllegalArgumentException("Test resource not found: " + name);
        }
        return stream;
    }

    public static byte[] bytes(String name) {
        try (InputStream stream = stream(name)) {
            return stream.readAllBytes();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read " + name, e);
        }
    }
}
