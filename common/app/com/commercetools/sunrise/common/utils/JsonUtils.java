package com.commercetools.sunrise.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import io.sphere.sdk.json.SphereJsonUtils;

public class JsonUtils {

    public static <T> T readCtpObject(final String resourcePath, final TypeReference<T> typeReference) {
        return SphereJsonUtils.readObjectFromResource(resourcePath, typeReference);
    }
}
