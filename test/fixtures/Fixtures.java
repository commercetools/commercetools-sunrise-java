package fixtures;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import io.sphere.sdk.utils.JsonUtils;

import java.io.IOException;
import java.net.URL;

//TODO move into SDK
public final class Fixtures {
    private Fixtures() {
    }

    public static <I, R> I readJsonFromClasspath(String resourcePath, TypeReference<R> typeReference) {
        final URL url = Resources.getResource(resourcePath);
        try {
            String categoriesJson = Resources.toString(url, Charsets.UTF_8);
            return JsonUtils.newObjectMapper().readValue(categoriesJson, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
