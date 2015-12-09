package productcatalog.models;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;

public class SortSelector extends Base {
    private String key;
    private List<SortOption<ProductProjection>> list;

    public SortSelector() {
    }

    public SortSelector(final String key, final List<SortOption<ProductProjection>> sortOptions) {
        this.key = key;
        this.list = sortOptions;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public List<SortOption<ProductProjection>> getList() {
        return list;
    }

    public void setList(final List<SortOption<ProductProjection>> list) {
        this.list = list;
    }
}
