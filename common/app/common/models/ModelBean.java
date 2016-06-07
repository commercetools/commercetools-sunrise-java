package common.models;

import io.sphere.sdk.models.Base;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class ModelBean extends Base {
    private final Map<String, Object> dynamic = new HashMap<>();

    public ModelBean() {
    }

    public final void addDynamic(final String attributeName, final Object object) {
        dynamic.put(attributeName, object);
    }

    public Map<String, Object> getDynamic() {
        return Collections.unmodifiableMap(dynamic);
    }
}
