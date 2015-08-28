package productcatalog.pages;

import java.util.List;

public class FilterListData {
    private final List<FacetData> facetData;

    public FilterListData(final List<FacetData> facetData) {
        this.facetData = facetData;
    }

    public List<FacetData> getList() {
        return facetData;
    }
}
