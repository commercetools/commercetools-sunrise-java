package productcatalog.productoverview.search;

import io.sphere.sdk.facets.SelectFacetBuilder;
import io.sphere.sdk.products.ProductProjection;

public class SelectFacetConfig extends FacetConfig<SelectFacetBuilder<ProductProjection>> {

    final SelectFacetBuilder<ProductProjection> selectFacetBuilder;

    public SelectFacetConfig(final SelectFacetBuilder<ProductProjection> facetBuilder, final double position) {
        super(facetBuilder, position);
        this.selectFacetBuilder = facetBuilder;
    }

    @Override
    public SelectFacetBuilder<ProductProjection> getFacetBuilder() {
        return selectFacetBuilder;
    }

    public static SelectFacetConfig of(final SelectFacetBuilder<ProductProjection> selectFacetBuilder, final double position) {
        return new SelectFacetConfig(selectFacetBuilder, position);
    }
}
