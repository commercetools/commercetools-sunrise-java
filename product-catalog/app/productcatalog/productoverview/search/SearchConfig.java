package productcatalog.productoverview.search;

public class SearchConfig {

    private final String paginationKey;
    private final String searchTermKey;
    private final ProductsPerPageConfig productsPerPageConfig;
    private final SortConfig sortConfig;
    private final FacetConfigList facetConfigList;

    private SearchConfig(final String paginationKey, final String searchTermKey, final ProductsPerPageConfig productsPerPageConfig,
                        final SortConfig sortConfig, final FacetConfigList facetConfigList) {
        this.paginationKey = paginationKey;
        this.searchTermKey = searchTermKey;
        this.productsPerPageConfig = productsPerPageConfig;
        this.sortConfig = sortConfig;
        this.facetConfigList = facetConfigList;
    }

    public String getPaginationKey() {
        return paginationKey;
    }

    public String getSearchTermKey() {
        return searchTermKey;
    }

    public ProductsPerPageConfig getProductsPerPageConfig() {
        return productsPerPageConfig;
    }

    public SortConfig getSortConfig() {
        return sortConfig;
    }

    public FacetConfigList getFacetConfigList() {
        return facetConfigList;
    }

    public static SearchConfig of(final String paginationKey, final String searchTermKey, final ProductsPerPageConfig productsPerPageConfig,
                                  final SortConfig sortConfig, final FacetConfigList facetConfigList) {
        return new SearchConfig(paginationKey, searchTermKey, productsPerPageConfig, sortConfig, facetConfigList);
    }
}
