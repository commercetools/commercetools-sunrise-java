package productcatalog.productoverview.search.facetedsearch;

import io.sphere.sdk.models.Base;

import java.util.List;

public final class FacetedSearchConfigList extends Base {

    private final List<SelectFacetedSearchConfig> selectFacetConfigList;
    // missing range facets


    private FacetedSearchConfigList(final List<SelectFacetedSearchConfig> selectFacetConfigList) {
        this.selectFacetConfigList = selectFacetConfigList;
    }

    public List<SelectFacetedSearchConfig> getSelectFacetedSearchConfigList() {
        return selectFacetConfigList;
    }

    public static FacetedSearchConfigList of(final List<SelectFacetedSearchConfig> selectFacetConfigList) {
        return new FacetedSearchConfigList(selectFacetConfigList);
    }
}
