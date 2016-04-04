package productcatalog.productoverview.search;

import common.contexts.UserContext;
import common.i18n.I18nResolver;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.FacetedSearchExpression;
import io.sphere.sdk.search.PagedSearchResult;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class FacetsCriteria {

    private final List<FacetCriteria> facets;

    private FacetsCriteria(final List<FacetCriteria> facets) {
        this.facets = facets;
    }

    public List<Facet<ProductProjection>> boundFacets(final PagedSearchResult<ProductProjection> searchResult) {
        return facets.stream()
                .map(facet -> facet.boundFacet(searchResult))
                .collect(toList());
    }

    public List<FacetedSearchExpression<ProductProjection>> getFacetedSearchExpressions() {
        return facets.stream()
                .map(FacetCriteria::getFacetedSearchExpression)
                .collect(toList());
    }

    public static FacetsCriteria of(final FacetsConfig facetsConfig, final Map<String, List<String>> queryString,
                                    final UserContext userContext, final I18nResolver i18nResolver,
                                    final List<Category> selectedCategories, final CategoryTree subcategoryTreeFacet) {
        final List<FacetCriteria> facetCriterias = facetsConfig.getFacetConfigs().stream()
                .map(facetConfig -> FacetCriteria.of(facetConfig, queryString, userContext, i18nResolver, selectedCategories, subcategoryTreeFacet))
                .collect(toList());
        return new FacetsCriteria(facetCriterias);
    }
}