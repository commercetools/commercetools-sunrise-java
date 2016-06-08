package productcatalog.productoverview.search.facetedsearch;

import io.sphere.sdk.facets.FacetBuilder;
import io.sphere.sdk.models.Base;

public abstract class FacetedSearchConfig<T> extends Base {

    private final FacetBuilder<T> facetBuilder;
    private final double position;

    protected FacetedSearchConfig(final FacetBuilder<T> facetBuilder, final double position) {
        this.facetBuilder = facetBuilder;
        this.position = position;
    }

    public FacetBuilder<T> getFacetBuilder() {
        return facetBuilder;
    }

    public double getPosition() {
        return position;
    }
}
