package productcatalog.models;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import productcatalog.models.SortOption;

import java.util.List;

public class SortSelector extends Base {
    private List<SortOption<ProductProjection>> list;

    public SortSelector() {
    }

    public SortSelector(final List<SortOption<ProductProjection>> sortOptions) {
        this.list = sortOptions;
    }

    public List<SortOption<ProductProjection>> getList() {
        return list;
    }

    public void setList(final List<SortOption<ProductProjection>> list) {
        this.list = list;
    }
}
