package productcatalog.productoverview.search.facetedsearch;

import io.sphere.sdk.facets.SelectFacetBuilder;
import io.sphere.sdk.products.ProductProjection;

public final class SelectFacetedSearchConfig extends FacetedSearchConfig<SelectFacetBuilder<ProductProjection>> {

    private final SelectFacetBuilder<ProductProjection> selectFacetBuilder;

    private SelectFacetedSearchConfig(final SelectFacetBuilder<ProductProjection> facetBuilder, final double position) {
        super(facetBuilder, position);
        this.selectFacetBuilder = facetBuilder;
    }

    @Override
    public SelectFacetBuilder<ProductProjection> getFacetBuilder() {
        return selectFacetBuilder;
    }

    public static SelectFacetedSearchConfig of(final SelectFacetBuilder<ProductProjection> selectFacetBuilder, final double position) {
        return new SelectFacetedSearchConfig(selectFacetBuilder, position);
    }
}
