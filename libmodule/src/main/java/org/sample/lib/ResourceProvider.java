package org.sample.lib;

import java.io.IOException;
import java.io.InputStream;

public class ResourceProvider {
    public static InputStream getResource(String name) throws IOException {
        return ResourceProvider.class.getModule().getResourceAsStream(name);
    }
}
