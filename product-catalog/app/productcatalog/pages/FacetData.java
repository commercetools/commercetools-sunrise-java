package productcatalog.pages;

import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.models.Base;

import static io.sphere.sdk.facets.DefaultFacetType.*;

public class FacetData extends Base {
    private Facet facet;

    public FacetData(final Facet<?> facet) {
        this.facet = facet;
    }

    public Facet getFacet() {
        return facet;
    }

    public boolean isSortedSelectFacet() {
        return facet.getType().equals(SORTED_SELECT);
    }

    public boolean isSelectFacet() {
        return facet.getType().equals(SELECT);
    }

    public boolean isSliderRangeFacet() {
        return facet.getType().equals(SLIDER_RANGE);
    }

    public boolean isBucketRangeFacet() {
        return facet.getType().equals(BUCKET_RANGE);
    }

    public boolean isHierarchicalSelectFacet() {
        return facet.getType().equals(HIERARCHICAL_SELECT);
    }
}
