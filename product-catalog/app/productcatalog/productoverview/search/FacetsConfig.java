package productcatalog.productoverview.search;

import io.sphere.sdk.models.Base;

import java.util.List;

public class FacetsConfig extends Base {

    private final List<FacetConfig> facetConfigs;

    private FacetsConfig(final List<FacetConfig> facetConfigs) {
        this.facetConfigs = facetConfigs;
    }

    public List<FacetConfig> getFacetConfigs() {
        return facetConfigs;
    }

    public static FacetsConfig of(final List<FacetConfig> facetConfigs) {
        return new FacetsConfig(facetConfigs);
    }
}
