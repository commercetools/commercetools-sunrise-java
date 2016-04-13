package search;

import com.google.inject.Inject;
import com.google.inject.Provider;
import play.Configuration;
import productcatalog.productoverview.search.*;

class SearchConfigProvider implements Provider<SearchConfig> {

    private Configuration configuration;
    private ProductsPerPageConfig productsPerPageConfig;
    private SortConfig sortConfig;
    private FacetConfigList facetConfigList;

    @Inject
    public SearchConfigProvider(final Configuration configuration, final ProductsPerPageConfig productsPerPageConfig,
                                final SortConfig sortConfig, final FacetConfigList facetConfigList) {
        this.configuration = configuration;
        this.productsPerPageConfig = productsPerPageConfig;
        this.sortConfig = sortConfig;
        this.facetConfigList = facetConfigList;
    }

    @Override
    public SearchConfig get() {
        final String searchProductsKey = configuration.getString("pop.searchTerm.key", "q");
        final String paginationKey = configuration.getString("pop.pagination.key", "page");
        return SearchConfig.of(paginationKey, searchProductsKey, productsPerPageConfig, sortConfig, facetConfigList);
    }

}
