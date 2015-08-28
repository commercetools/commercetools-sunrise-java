package productcatalog.pages;

import io.sphere.sdk.models.Base;

import java.util.List;

public class FilterListData extends Base {
    private final List<FacetData> facetData;

    public FilterListData(final List<FacetData> facetData) {
        this.facetData = facetData;
    }

    public List<FacetData> getList() {
        return facetData;
    }
}
