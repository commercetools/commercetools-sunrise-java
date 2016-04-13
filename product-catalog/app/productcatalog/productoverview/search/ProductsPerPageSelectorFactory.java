package productcatalog.productoverview.search;

import java.util.List;
import java.util.Map;

public class ProductsPerPageSelectorFactory extends SelectorFactory<ProductsPerPageSelector> {

    private final ProductsPerPageConfig config;

    public ProductsPerPageSelectorFactory(final Map<String, List<String>> queryString, final ProductsPerPageConfig config) {
        super(queryString);
        this.config = config;
    }

    @Override
    public ProductsPerPageSelector create() {
        return ProductsPerPageSelector.of(key(), config.getOptions(), config.getDefaultAmount(), selectedValues());
    }

    @Override
    protected String key() {
        return config.getKey();
    }

    public static ProductsPerPageSelectorFactory of(final ProductsPerPageConfig productsPerPageConfig,
                                                    final Map<String, List<String>> queryString) {
        return new ProductsPerPageSelectorFactory(queryString, productsPerPageConfig);
    }
}
