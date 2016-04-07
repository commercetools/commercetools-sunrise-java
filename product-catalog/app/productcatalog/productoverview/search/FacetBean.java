package productcatalog.productoverview.search;

import common.contexts.UserContext;
import common.i18n.I18nIdentifier;
import common.i18n.I18nResolver;
import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.facets.FacetType;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

public class FacetBean extends Base {
    private Facet facet;

    public FacetBean() {
    }

    public FacetBean(final FacetCriteria facetCriteria, final PagedSearchResult<ProductProjection> searchResult,
                     final UserContext userContext, final I18nResolver i18nResolver) {
        final Facet<ProductProjection> facet = facetCriteria.getFacet(searchResult);
        final String label = facetCriteria.getFacetConfig().getLabel();
        final String resolvedLabel = i18nResolver.get(userContext.locales(), I18nIdentifier.of(label))
                .orElse(label);
        this.facet = facet.withLabel(resolvedLabel);
    }

    public Facet getFacet() {
        return facet;
    }

    public void setFacet(final Facet facet) {
        this.facet = facet;
    }

    public boolean isDisplayList() {
        return isFacetType(SunriseFacetType.SELECT_LIST_DISPLAY);
    }

    public boolean isSelectFacet() {
        return isFacetType(SunriseFacetType.SELECT_LIST_DISPLAY) || isFacetType(SunriseFacetType.SELECT_TWO_COLUMNS_DISPLAY);
    }

    public boolean isSliderRangeFacet() {
        return false; //TODO implement
    }

    public boolean isBucketRangeFacet() {
        return false; //TODO implement
    }

    public boolean isHierarchicalSelectFacet() {
        return isFacetType(SunriseFacetType.SELECT_CATEGORY_HIERARCHICAL_DISPLAY);
    }

    private boolean isFacetType(final FacetType type) {
        return facet.getType().equals(type);
    }
}
