package productcatalog.productoverview.search;

import java.util.List;

public class SearchConfig {

    private final String paginationKey;
    private final String searchTermKey;
    private final DisplayConfig displayConfig;
    private final SortConfig sortConfig;
    private final List<FacetConfig> facetsConfig;

    private SearchConfig(final String paginationKey, final String searchTermKey, final DisplayConfig displayConfig,
                        final SortConfig sortConfig, final List<FacetConfig> facetsConfig) {
        this.paginationKey = paginationKey;
        this.searchTermKey = searchTermKey;
        this.displayConfig = displayConfig;
        this.sortConfig = sortConfig;
        this.facetsConfig = facetsConfig;
    }

    public String getPaginationKey() {
        return paginationKey;
    }

    public String getSearchTermKey() {
        return searchTermKey;
    }

    public DisplayConfig getDisplayConfig() {
        return displayConfig;
    }

    public SortConfig getSortConfig() {
        return sortConfig;
    }

    public List<FacetConfig> getFacetsConfig() {
        return facetsConfig;
    }

    public static SearchConfig of(final String paginationKey, final String searchTermKey, final DisplayConfig displayConfig,
                                  final SortConfig sortConfig, final List<FacetConfig> facetsConfig) {
        return new SearchConfig(paginationKey, searchTermKey, displayConfig, sortConfig, facetsConfig);
    }
}
