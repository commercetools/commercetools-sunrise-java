package productcatalog.productoverview.search;

import io.sphere.sdk.models.Base;

import java.util.List;

public class FacetConfigList extends Base {

    private final List<SelectFacetConfig> selectFacetConfigList;
    // missing range facets


    public FacetConfigList(final List<SelectFacetConfig> selectFacetConfigList) {
        this.selectFacetConfigList = selectFacetConfigList;
    }

    public List<SelectFacetConfig> getSelectFacetConfigs() {
        return selectFacetConfigList;
    }

    public static FacetConfigList of(final List<SelectFacetConfig> selectFacetConfigList) {
        return new FacetConfigList(selectFacetConfigList);
    }
}
