package productcatalog.pages;

import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.facets.FacetType;

public class FacetData<T> {
    private Facet facet;

    public FacetData(final Facet<?> facet) {
        this.facet = facet;
    }

    public Facet getFacet() {
        return facet;
    }

    public boolean isFlexibleSelectFacet() {
        return facet.getType().equals(FacetType.FLEXIBLE_SELECT);
    }

    public boolean isSelectFacet() {
        return facet.getType().equals(FacetType.SELECT);
    }

    public boolean isSliderRangeFacet() {
        return facet.getType().equals(FacetType.SLIDER_RANGE);
    }

    public boolean isBucketRangeFacet() {
        return facet.getType().equals(FacetType.BUCKET_RANGE);
    }

    public boolean isHierarchicalSelectFacet() {
        return facet.getType().equals(FacetType.HIERARCHICAL_SELECT);
    }
}
