package productcatalog.productoverview.search;

import common.contexts.UserContext;
import common.template.i18n.I18nIdentifier;
import common.template.i18n.I18nResolver;
import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.facets.FacetType;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

public class FacetSelectorBean extends Base {
    private Facet facet;

    public FacetSelectorBean() {
    }

    public FacetSelectorBean(final FacetSelector facetSelector, final PagedSearchResult<ProductProjection> searchResult,
                             final UserContext userContext, final I18nResolver i18nResolver) {
        final Facet<ProductProjection> facet = facetSelector.getFacet(searchResult);
        if (facet.getLabel() != null) {
            final String label = i18nResolver.getOrKey(userContext.locales(), I18nIdentifier.of(facet.getLabel()));
            this.facet = facet.withLabel(label);
        } else {
            this.facet = facet;
        }
    }

    public Facet getFacet() {
        return facet;
    }

    public void setFacet(final Facet facet) {
        this.facet = facet;
    }

    public boolean isDisplayList() {
        return isFacetType(SunriseFacetType.LIST);
    }

    public boolean isSelectFacet() {
        return isFacetType(SunriseFacetType.LIST) || isFacetType(SunriseFacetType.COLUMNS_LIST);
    }

    public boolean isSliderRangeFacet() {
        return false; //TODO implement
    }

    public boolean isBucketRangeFacet() {
        return false; //TODO implement
    }

    public boolean isHierarchicalSelectFacet() {
        return isFacetType(SunriseFacetType.CATEGORY_TREE);
    }

    private boolean isFacetType(final FacetType type) {
        return facet.getType().equals(type);
    }
}
