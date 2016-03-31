package productcatalog.productoverview.search;

import java.util.List;

public class SearchConfig {

    private final String paginationKey;
    private final String searchTermKey;
    private final SortConfig sortConfig;
    private final DisplayConfig displayConfig;
    private final List<String> sortedSizes;

    public SearchConfig(final String paginationKey, final String searchTermKey, final SortConfig sortConfig,
                        final DisplayConfig displayConfig, final List<String> sortedSizes) {
        this.paginationKey = paginationKey;
        this.searchTermKey = searchTermKey;
        this.sortConfig = sortConfig;
        this.displayConfig = displayConfig;
        this.sortedSizes = sortedSizes;
    }

    public String getPaginationKey() {
        return paginationKey;
    }

    public String getSearchTermKey() {
        return searchTermKey;
    }

    public SortConfig getSortConfig() {
        return sortConfig;
    }

    public DisplayConfig getDisplayConfig() {
        return displayConfig;
    }

    public List<String> getSortedSizes() {
        return sortedSizes;
    }
}
