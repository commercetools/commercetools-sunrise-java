package productcatalog.productoverview.search;

import io.sphere.sdk.models.Base;

import java.util.List;

public class SortConfig extends Base {

    private final String key;
    private final List<SortOption> options;
    private final List<String> defaultValue;

    private SortConfig(final String key, final List<SortOption> options, final List<String> defaultValue) {
        this.key = key;
        this.options = options;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public List<SortOption> getOptions() {
        return options;
    }

    public List<String> getDefaultValue() {
        return defaultValue;
    }

    public static SortConfig of(final String key, final List<SortOption> options, final List<String> defaultValue) {
        return new SortConfig(key, options, defaultValue);
    }
}
