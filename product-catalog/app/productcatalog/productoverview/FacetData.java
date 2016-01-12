package productcatalog.productoverview;

import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.facets.FacetType;
import io.sphere.sdk.models.Base;

import static productcatalog.models.SunriseFacetType.*;

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

    public boolean isDisplayList() {
        return isFacetType(SELECT_LIST_DISPLAY);
    }

    public boolean isSelectFacet() {
        return isFacetType(SELECT_LIST_DISPLAY) || isFacetType(SELECT_TWO_COLUMNS_DISPLAY);
    }

    public boolean isSliderRangeFacet() {
        return false; //TODO implement
    }

    public boolean isBucketRangeFacet() {
        return false; //TODO implement
    }

    public boolean isHierarchicalSelectFacet() {
        return isFacetType(SELECT_CATEGORY_HIERARCHICAL_DISPLAY);
    }

    private boolean isFacetType(final FacetType type) {
        return facet.getType().equals(type);
    }
}
