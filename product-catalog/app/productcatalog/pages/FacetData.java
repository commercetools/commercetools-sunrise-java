package productcatalog.pages;

import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.facets.FacetType;
import io.sphere.sdk.models.Base;

import static io.sphere.sdk.facets.DefaultFacetType.*;

public class FacetData extends Base {
    private Facet facet;

    public FacetData() {
    }

    public FacetData(final Facet facet) {
        this.facet = facet;
    }

    public Facet getFacet() {
        return facet;
    }

    public void setFacet(final Facet facet) {
        this.facet = facet;
    }

    public boolean isSelectFacet() {
        return isFacetType(SELECT) || isFacetType(SORTED_SELECT);
    }

    public boolean isSliderRangeFacet() {
        return isFacetType(SLIDER_RANGE);
    }

    public boolean isBucketRangeFacet() {
        return isFacetType(BUCKET_RANGE);
    }

    public boolean isHierarchicalSelectFacet() {
        return isFacetType(HIERARCHICAL_SELECT);
    }

    private boolean isFacetType(final FacetType type) {
        return facet.getType().equals(type);
    }
}
