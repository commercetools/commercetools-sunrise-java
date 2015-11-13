package productcatalog.pages;

import io.sphere.sdk.models.Base;

import java.util.List;

public class FilterListData extends Base {
    private final List<FacetData> facetData;
    private final String url;
    private final String searchTerm;

    public FilterListData(final String url, final List<FacetData> facetData, final String searchTerm) {
        this.url = url;
        this.facetData = facetData;
        this.searchTerm = searchTerm;
    }

    public String getUrl() {
        return url;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public List<FacetData> getList() {
        return facetData;
    }
}
