package productcatalog.pages;

import io.sphere.sdk.models.Base;

import java.util.List;

public class FilterListData extends Base {
    private List<FacetData> facetData;
    private String url;
    private String searchTerm;

    public FilterListData() {
    }

    public List<FacetData> getFacetData() {
        return facetData;
    }

    public void setFacetData(final List<FacetData> facetData) {
        this.facetData = facetData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(final String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
