package common.categories;

import com.fasterxml.jackson.core.type.TypeReference;

import static io.sphere.sdk.json.SphereJsonUtils.readObjectFromResource;

public class JsonUtils {

    public static <T> T readJson(final String resourceName, final TypeReference<T> typeReference) {
        return readObjectFromResource(resourceName, typeReference);
    }

}
