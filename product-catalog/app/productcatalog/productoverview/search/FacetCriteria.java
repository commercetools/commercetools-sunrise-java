package productcatalog.productoverview.search;

import common.contexts.UserContext;
import common.i18n.I18nIdentifier;
import common.i18n.I18nResolver;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.facets.*;
import io.sphere.sdk.models.Identifiable;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.FacetedSearchExpression;
import io.sphere.sdk.search.PagedSearchResult;
import io.sphere.sdk.search.model.TermFacetedSearchSearchModel;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class FacetCriteria {

    private final Facet<ProductProjection> facet;

    private FacetCriteria(Facet<ProductProjection> facet) {
        this.facet = facet;
    }

    public Facet<ProductProjection> boundFacet(final PagedSearchResult<ProductProjection> searchResult) {
        final Facet<ProductProjection> facet = this.facet.withSearchResult(searchResult);
        System.out.println(facet.getType());
        return facet;
    }

    public FacetedSearchExpression<ProductProjection> getFacetedSearchExpression() {
        return facet.getFacetedSearchExpression();
    }

    public static FacetCriteria of(final FacetConfig facetConfig, final Map<String, List<String>> queryString,
                                   final UserContext userContext, final I18nResolver i18nResolver,
                                   final List<Category> selectedCategories, final CategoryTree categoryTree) {
        final Facet<ProductProjection> facet = initializeFacet(facetConfig, selectedCategories, queryString, userContext, i18nResolver, categoryTree);
        return new FacetCriteria(facet);
    }

    private static Facet<ProductProjection> initializeFacet(final FacetConfig facetConfig, final List<Category> selectedCategories,
                                                            final Map<String, List<String>> queryString, final UserContext userContext,
                                                            final I18nResolver i18nResolver, final CategoryTree categoryTree) {
        switch (facetConfig.getType()) {
            case SELECT_CATEGORY_HIERARCHICAL_DISPLAY:
            case SELECT_TWO_COLUMNS_DISPLAY:
            case SELECT_LIST_DISPLAY:
            default:
                return initializeSelectFacet(facetConfig, selectedCategories, queryString, userContext, i18nResolver, categoryTree);
        }
    }

    private static Facet<ProductProjection> initializeSelectFacet(final FacetConfig facetConfig, final List<Category> selectedCategories,
                                                                  final Map<String, List<String>> queryString, final UserContext userContext,
                                                                  final I18nResolver i18nResolver, final CategoryTree categoryTree) {
        final String label = i18nResolver.get(userContext.locales(), I18nIdentifier.of(facetConfig.getLabel()))
                .orElse(facetConfig.getLabel());
        final FacetOptionMapper mapper = initializeMapper(facetConfig, selectedCategories, userContext, categoryTree).orElse(null);
        final List<String> selectedValues = getSelectedValues(facetConfig, mapper, queryString, selectedCategories);
        return SelectFacetBuilder.of(facetConfig.getKey(), label, getSearchModel(facetConfig, userContext))
                .type(facetConfig.getType())
                .countHidden(!facetConfig.isCountShown())
                .matchingAll(facetConfig.isMatchingAll())
                .multiSelect(facetConfig.isMultiSelect())
                .limit(facetConfig.getLimit().orElse(null))
                .threshold(facetConfig.getThreshold().orElse(null))
                .mapper(mapper)
                .selectedValues(selectedValues)
                .build();
    }

    private static List<String> getSelectedValues(final FacetConfig facetConfig, final @Nullable FacetOptionMapper mapper,
                                                  final Map<String, List<String>> queryString, final List<Category> selectedCategories) {
        if (mapper instanceof HierarchicalCategoryFacetOptionMapper) {
            return selectedCategories.stream()
                    .map(Category::getId)
                    .collect(toList());
        } else {
            return queryString.getOrDefault(facetConfig.getKey(), emptyList());
        }
    }

    private static TermFacetedSearchSearchModel<ProductProjection> getSearchModel(final FacetConfig facetConfig,
                                                                                  final UserContext userContext) {
        final String expr = facetConfig.getExpr().replaceAll("\\{\\{locale\\}\\}", userContext.locale().toLanguageTag());
        return TermFacetedSearchSearchModel.of(expr);
    }

    private static Optional<FacetOptionMapper> initializeMapper(final FacetConfig facetConfig, final List<Category> selectedCategories,
                                                                final UserContext userContext, final CategoryTree categoryTree) {
        return facetConfig.getMapperConfig()
                .map(mapperConfig -> {
                    switch (mapperConfig.getType()) {
                        case "hierarchicalCategoryFacet":
                            final CategoryTree categoriesInFacet = getCategoriesInFacet(selectedCategories, categoryTree);
                            return HierarchicalCategoryFacetOptionMapper.of(selectedCategories, categoriesInFacet, userContext.locales());
                        case "alphabeticallySortedFacet":
                            return AlphabeticallySortedFacetOptionMapper.of();
                        case "customSortedFacet":
                            return CustomSortedFacetOptionMapper.of(mapperConfig.getValues());
                        default:
                            return null;
                    }
                });
    }

    private static CategoryTree getCategoriesInFacet(final List<Category> selectedCategories, final CategoryTree categoryTree) {
        final List<Identifiable<Category>> categories = selectedCategories.stream()
                .flatMap(category -> {
                    final List<Identifiable<Category>> relatives = new ArrayList<>();
                    relatives.add(categoryTree.getRootAncestor(category));
                    //relatives.addAll(categoryTree.findSiblings(singletonList(category)));
                    //relatives.addAll(categoryTree.findChildren(category));
                    return relatives.stream();
                })
                .collect(toList());

        return categoryTree.getSubtree(categories);
    }
}
