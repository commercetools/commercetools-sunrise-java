package productcatalog.pages;

import java.util.List;

public class FilterListData {
    private final List<SelectFacetData> facetData;

    public FilterListData(final List<SelectFacetData> facetData) {
        this.facetData = facetData;
    }

    public List<SelectFacetData> getList() {
        return facetData;
    }
}
