package productcatalog.productoverview.search.sort;

import io.sphere.sdk.models.Base;

import java.util.List;

public final class SortConfig extends Base {

    private final String key;
    private final List<SortOption> options;

    private SortConfig(final String key, final List<SortOption> options) {
        this.key = key;
        this.options = options;
    }

    public String getKey() {
        return key;
    }

    public List<SortOption> getOptions() {
        return options;
    }

    public static SortConfig of(final String key, final List<SortOption> options) {
        return new SortConfig(key, options);
    }
}
