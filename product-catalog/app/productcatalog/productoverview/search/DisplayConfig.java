package productcatalog.productoverview.search;

import io.sphere.sdk.models.Base;

import java.util.List;

public class DisplayConfig extends Base {

    private final String key;
    private final List<Integer> options;
    private final int defaultValue;

    private DisplayConfig(final String key, final List<Integer> options, final int defaultValue) {
        this.key = key;
        this.options = options;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public List<Integer> getOptions() {
        return options;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public static DisplayConfig of(final String key, final List<Integer> options, final int defaultValue) {
        return new DisplayConfig(key, options, defaultValue);
    }
}
