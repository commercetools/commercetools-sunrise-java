package common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.sphere.sdk.json.SphereJsonUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonUtils {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static JsonNode readJsonNode(final String resourcePath) throws IOException {
        try(final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            final String jsonAsString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            IOUtils.closeQuietly(inputStream);
            return OBJECT_MAPPER.readTree(jsonAsString);
        }
    }

    public static <T> T readCtpObject(final String resourcePath, final TypeReference<T> typeReference) {
        return SphereJsonUtils.readObjectFromResource(resourcePath, typeReference);
    }
}
