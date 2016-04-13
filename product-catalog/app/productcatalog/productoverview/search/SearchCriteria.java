package productcatalog.productoverview.search;

import java.util.List;

public class SearchCriteria {

    private final int page;
    private final SearchBox searchBox;
    private final SortSelector sortSelector;
    private final ProductsPerPageSelector productsPerPageSelector;
    private final List<FacetSelector> facetSelectors;

    private SearchCriteria(final int page, final SearchBox searchBox, final ProductsPerPageSelector productsPerPageSelector,
                           final SortSelector sortSelector, final List<FacetSelector> facetSelectors) {
        this.page = page;
        this.searchBox = searchBox;
        this.productsPerPageSelector = productsPerPageSelector;
        this.sortSelector = sortSelector;
        this.facetSelectors = facetSelectors;
    }

    public int getPage() {
        return page;
    }

    public SearchBox getSearchBox() {
        return searchBox;
    }

    public ProductsPerPageSelector getProductsPerPageSelector() {
        return productsPerPageSelector;
    }

    public SortSelector getSortSelector() {
        return sortSelector;
    }

    public List<FacetSelector> getFacetSelectors() {
        return facetSelectors;
    }

    public static SearchCriteria of(final int page, final SearchBox searchBox, final ProductsPerPageSelector productsPerPageSelector,
                                    final SortSelector sortSelector, final List<FacetSelector> facetSelectors) {
        return new SearchCriteria(page, searchBox, productsPerPageSelector, sortSelector, facetSelectors);
    }
}
