package productcatalog.productoverview.search;

import common.contexts.NoLocaleFoundException;
import common.contexts.UserContext;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.LocalizedStringEntry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class SearchCriteria {

    private final int page;
    private final Optional<LocalizedStringEntry> searchTerm;
    private final SortSelector sortSelector;
    private final ProductsPerPageSelector productsPerPageSelector;
    private final List<FacetSelector> facetSelectors;

    private SearchCriteria(final int page, @Nullable final LocalizedStringEntry searchTerm, final ProductsPerPageSelector productsPerPageSelector,
                           final SortSelector sortSelector, final List<FacetSelector> facetSelectors) {
        this.page = page;
        this.searchTerm = Optional.ofNullable(searchTerm);
        this.productsPerPageSelector = productsPerPageSelector;
        this.sortSelector = sortSelector;
        this.facetSelectors = facetSelectors;
    }

    public int getPage() {
        return page;
    }

    public Optional<LocalizedStringEntry> getSearchTerm() {
        return searchTerm;
    }

    public ProductsPerPageSelector getProductsPerPageSelector() {
        return productsPerPageSelector;
    }

    public SortSelector getSortSelector() {
        return sortSelector;
    }

    public List<FacetSelector> getFacetSelectors() {
        return facetSelectors;
    }

    public static SearchCriteria of(final int page, final SearchConfig searchConfig, final Map<String, List<String>> queryString,
                                    final UserContext userContext, final CategoryTree categoryTree,
                                    final List<Category> selectedCategories) {
        final LocalizedStringEntry searchTerm = getSearchTerm(searchConfig, queryString, userContext.locales()).orElse(null);
        final ProductsPerPageSelector productsPerPageSelector = ProductsPerPageSelectorFactory.of(searchConfig.getProductsPerPageConfig(), queryString).create();
        final SortSelector sortSelector = SortSelectorFactory.of(searchConfig.getSortConfig(), queryString, userContext).create();
        final List<FacetSelector> facetSelectors = getFacetSelectors(searchConfig, queryString, userContext, categoryTree, selectedCategories);
        return new SearchCriteria(page, searchTerm, productsPerPageSelector, sortSelector, facetSelectors);
    }

    public static SearchCriteria of(final int page, final SearchConfig searchConfig, final Map<String, List<String>> queryString,
                                    final UserContext userContext) {
        return of(page, searchConfig, queryString, userContext, CategoryTree.of(emptyList()), emptyList());
    }

    public static Optional<LocalizedStringEntry> getSearchTerm(final SearchConfig searchConfig, final Map<String, List<String>> queryString,
                                                               final List<Locale> locales) {
        final Locale locale = locales.stream().findFirst().orElseThrow(NoLocaleFoundException::new);
        final String searchTerm = getSelectedValues(searchConfig.getSearchTermKey(), queryString).stream()
                .collect(joining(" ")).trim();
        return searchTerm.isEmpty() ? Optional.empty() : Optional.of(LocalizedStringEntry.of(locale, searchTerm));
    }

    private static List<FacetSelector> getFacetSelectors(final SearchConfig searchConfig, final Map<String, List<String>> queryString,
                                                         final UserContext userContext, final CategoryTree categoryTree, final List<Category> selectedCategories) {
        // missing range facets
        return getSelectFacetSelectors(searchConfig, queryString, userContext, categoryTree, selectedCategories);
    }

    private static List<FacetSelector> getSelectFacetSelectors(final SearchConfig searchConfig, final Map<String, List<String>> queryString,
                                                               final UserContext userContext, final CategoryTree categoryTree, final List<Category> selectedCategories) {
        return searchConfig.getFacetConfigList().getSelectFacetConfigs().stream()
                .map(facetConfig -> SelectFacetSelectorFactory.of(facetConfig, queryString, selectedCategories, userContext, categoryTree).create())
                .collect(toList());
    }

    private static List<String> getSelectedValues(final String key, final Map<String, List<String>> queryString) {
        return queryString.getOrDefault(key, emptyList());
    }
}
