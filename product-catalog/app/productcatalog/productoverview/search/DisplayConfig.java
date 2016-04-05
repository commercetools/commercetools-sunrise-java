package productcatalog.productoverview.search;

import io.sphere.sdk.models.Base;

import java.util.List;

public class DisplayConfig extends Base {

    private final String key;
    private final List<Integer> options;
    private final int defaultValue;
    private final boolean enableAll;

    private DisplayConfig(final String key, final List<Integer> options, final int defaultValue, final boolean enableAll) {
        this.key = key;
        this.options = options;
        this.defaultValue = defaultValue;
        this.enableAll = enableAll;
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

    public boolean isEnableAll() {
        return enableAll;
    }

    public static DisplayConfig of(final String key, final List<Integer> options, final int defaultValue, final boolean enableAll) {
        return new DisplayConfig(key, options, defaultValue, enableAll);
    }
}
