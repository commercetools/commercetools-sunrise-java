package productcatalog.productoverview.search;

public class SearchConfig {

    private final String paginationKey;
    private final String searchTermKey;
    private final DisplayConfig displayConfig;
    private final SortConfig sortConfig;
    private final FacetsConfig facetsConfig;

    public SearchConfig(final String paginationKey, final String searchTermKey, final DisplayConfig displayConfig,
                        final SortConfig sortConfig, final FacetsConfig facetsConfig) {
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

    public FacetsConfig getFacetsConfig() {
        return facetsConfig;
    }
}
