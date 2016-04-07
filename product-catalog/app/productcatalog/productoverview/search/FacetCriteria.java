package productcatalog.productoverview.search;

import common.contexts.NoLocaleFoundException;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.facets.*;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.FacetedSearchExpression;
import io.sphere.sdk.search.PagedSearchResult;
import io.sphere.sdk.search.model.TermFacetedSearchSearchModel;

import javax.annotation.Nullable;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class FacetCriteria {

    private final FacetConfig facetConfig;
    private final Facet<ProductProjection> facet;

    private FacetCriteria(final FacetConfig facetConfig, Facet<ProductProjection> facet) {
        this.facetConfig = facetConfig;
        this.facet = facet;
    }

    public FacetConfig getFacetConfig() {
        return facetConfig;
    }

    public Facet<ProductProjection> getFacet(final PagedSearchResult<ProductProjection> searchResult) {
        return facet.withSearchResult(searchResult);
    }

    public FacetedSearchExpression<ProductProjection> getFacetedSearchExpression() {
        return facet.getFacetedSearchExpression();
    }

    public static FacetCriteria of(final FacetConfig facetConfig, final Map<String, List<String>> queryString,
                                   final List<Locale> locales, final List<Category> selectedCategories, final CategoryTree categoryTree) {
        final Facet<ProductProjection> facet = initializeFacet(facetConfig, selectedCategories, queryString, locales, categoryTree);
        return new FacetCriteria(facetConfig, facet);
    }

    private static Facet<ProductProjection> initializeFacet(final FacetConfig facetConfig, final List<Category> selectedCategories,
                                                            final Map<String, List<String>> queryString, final List<Locale> locales,
                                                            final CategoryTree categoryTree) {
        switch (facetConfig.getType()) {
            case SELECT_CATEGORY_HIERARCHICAL_DISPLAY:
            case SELECT_TWO_COLUMNS_DISPLAY:
            case SELECT_LIST_DISPLAY:
            default:
                return initializeSelectFacet(facetConfig, selectedCategories, queryString, locales, categoryTree);
        }
    }

    private static Facet<ProductProjection> initializeSelectFacet(final FacetConfig facetConfig, final List<Category> selectedCategories,
                                                                  final Map<String, List<String>> queryString, final List<Locale> locales,
                                                                  final CategoryTree categoryTree) {
        final FacetOptionMapper mapper = initializeMapper(facetConfig, selectedCategories, locales, categoryTree).orElse(null);
        final List<String> selectedValues = getSelectedValues(facetConfig, mapper, queryString, selectedCategories, categoryTree);
        return SelectFacetBuilder.of(facetConfig.getKey(), getSearchModel(facetConfig, locales))
                .type(facetConfig.getType())
                .label(facetConfig.getLabel())
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
                                                  final Map<String, List<String>> queryString,
                                                  final List<Category> selectedCategories, final CategoryTree categoryTree) {
        if (mapper instanceof HierarchicalCategoryFacetOptionMapper) {
            return selectedCategories.stream()
                    .flatMap(category -> categoryTree.getSubtree(singletonList(category)).getAllAsFlatList().stream())
                    .map(Category::getId)
                    .distinct()
                    .collect(toList());
        } else {
            return queryString.getOrDefault(facetConfig.getKey(), emptyList());
        }
    }

    private static TermFacetedSearchSearchModel<ProductProjection> getSearchModel(final FacetConfig facetConfig,
                                                                                  final List<Locale> locales) {
        final Locale locale = locales.stream().findFirst().orElseThrow(NoLocaleFoundException::new);
        final String expr = facetConfig.getExpr().replaceAll("\\{\\{locale\\}\\}", locale.toLanguageTag());
        return TermFacetedSearchSearchModel.of(expr);
    }

    private static Optional<FacetOptionMapper> initializeMapper(final FacetConfig facetConfig, final List<Category> selectedCategories,
                                                                final List<Locale> locales, final CategoryTree categoryTree) {
        return facetConfig.getMapperConfig()
                .map(mapperConfig -> {
                    switch (mapperConfig.getType()) {
                        case "hierarchicalCategoryFacet":
                            final CategoryTree categoriesInFacet = getCategoriesInFacet(selectedCategories, categoryTree);
                            return HierarchicalCategoryFacetOptionMapper.of(selectedCategories, categoriesInFacet, locales);
                        case "alphabeticallySortedFacet":
                            return AlphabeticallySortedFacetOptionMapper.of();
                        case "customSortedFacet":
                            return CustomSortedFacetOptionMapper.of(mapperConfig.getValues());
                        default:
                            return null;
                    }
                });
    }

    /**
     * Forms the category tree displayed in the category facet. That is, the tree containing:
     * - the category itself
     * - the ancestors tree
     * - the direct children categories
     * - the sibling categories
     * @param selectedCategories list of the selected categories
     * @param categoryTree the original category tree
     * @return the new category tree containing only those categories displayed in the category facet
     */
    private static CategoryTree getCategoriesInFacet(final List<Category> selectedCategories, final CategoryTree categoryTree) {
        final List<Category> categoriesInFacet = selectedCategories.stream()
                .flatMap(category -> {
                    final List<Category> categories = new ArrayList<>();
                    categories.add(category);
                    categories.addAll(category.getAncestors().stream()
                            .map(c -> categoryTree.findById(c.getId()).orElse(null))
                            .filter(c -> c != null)
                            .collect(toList()));
                    categories.addAll(categoryTree.findChildren(category));
                    categories.addAll(categoryTree.findSiblings(singletonList(category)));
                    return categories.stream();
                })
                .distinct()
                .collect(toList());
        return CategoryTree.of(categoriesInFacet);
    }
}
