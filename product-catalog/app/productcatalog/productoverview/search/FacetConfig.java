package productcatalog.productoverview.search;

import io.sphere.sdk.facets.FacetBuilder;
import io.sphere.sdk.facets.SelectFacetBuilder;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;

public abstract class FacetConfig<T> extends Base {

    private final FacetBuilder<T> facetBuilder;
    private final double position;

    protected FacetConfig(final FacetBuilder<T> facetBuilder, final double position) {
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
