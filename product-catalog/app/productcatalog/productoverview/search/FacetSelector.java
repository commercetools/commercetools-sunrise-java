package productcatalog.productoverview.search;

import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.FacetedSearchExpression;
import io.sphere.sdk.search.PagedSearchResult;

import java.util.List;

public class FacetSelector extends Base {

    private final Facet<ProductProjection> facet;
    private final double position;

    private FacetSelector(Facet<ProductProjection> facet, final double position) {
        this.facet = facet;
        this.position = position;
    }

    public Facet<ProductProjection> getFacet(final PagedSearchResult<ProductProjection> searchResult) {
        return facet.withSearchResult(searchResult);
    }

    public double getPosition() {
        return position;
    }

    public FacetedSearchExpression<ProductProjection> getFacetedSearchExpression() {
        return facet.getFacetedSearchExpression();
    }

    public static FacetSelector of(final Facet<ProductProjection> facet, final double position, final List<String> selectedValues) {
        return new FacetSelector(facet.withSelectedValues(selectedValues), position);
    }
}
