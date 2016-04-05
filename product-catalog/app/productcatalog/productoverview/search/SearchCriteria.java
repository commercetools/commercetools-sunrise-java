package productcatalog.productoverview.search;

import common.contexts.UserContext;
import common.i18n.I18nResolver;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.LocalizedStringEntry;
import io.sphere.sdk.products.search.ProductProjectionFacetedSearchSearchModel;
import io.sphere.sdk.products.search.ProductProjectionSearchModel;
import play.mvc.Http;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static common.utils.UrlUtils.getQueryString;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;

public class SearchCriteria {
    private static final ProductProjectionFacetedSearchSearchModel FACET = ProductProjectionSearchModel.of().facetedSearch();

    private final int page;
    private final Optional<LocalizedStringEntry> searchTerm;
    private final SortCriteria sortCriteria;
    private final DisplayCriteria displayCriteria;
    private final FacetsCriteria facetsCriteria;

    private SearchCriteria(final int page, @Nullable final LocalizedStringEntry searchTerm, final DisplayCriteria displayCriteria,
                           final SortCriteria sortCriteria, final FacetsCriteria facetsCriteria) {
        this.page = page;
        this.searchTerm = Optional.ofNullable(searchTerm);
        this.displayCriteria = displayCriteria;
        this.sortCriteria = sortCriteria;
        this.facetsCriteria = facetsCriteria;
    }

    public int getPage() {
        return page;
    }

    public Optional<LocalizedStringEntry> getSearchTerm() {
        return searchTerm;
    }

    public DisplayCriteria getDisplayCriteria() {
        return displayCriteria;
    }

    public SortCriteria getSortCriteria() {
        return sortCriteria;
    }

    public FacetsCriteria getFacetsCriteria() {
        return facetsCriteria;
    }

    public static SearchCriteria of(final int page, final SearchConfig searchConfig, final Http.Request request,
                                    final I18nResolver i18nResolver, final UserContext userContext,
                                    final CategoryTree subcategoryTreeFacet, final List<Category> selectedCategories) {
        final Map<String, List<String>> queryString = getQueryString(request);
        final LocalizedStringEntry searchTerm = getSearchTerm(searchConfig, queryString, userContext).orElse(null);
        final DisplayCriteria displayCriteria = DisplayCriteria.of(searchConfig.getDisplayConfig(), queryString, userContext, i18nResolver);
        final SortCriteria sortCriteria = SortCriteria.of(searchConfig.getSortConfig(), queryString, userContext, i18nResolver);
        final FacetsCriteria facetsCriteria = FacetsCriteria.of(searchConfig.getFacetsConfig(), queryString, userContext, i18nResolver, selectedCategories, subcategoryTreeFacet);
        return new SearchCriteria(page, searchTerm, displayCriteria, sortCriteria, facetsCriteria);
    }

    public static SearchCriteria of(final int page, final SearchConfig searchConfig, final Http.Request request,
                                    final I18nResolver i18nResolver, final UserContext userContext) {
        return of(page, searchConfig, request, i18nResolver, userContext, CategoryTree.of(emptyList()), emptyList());
    }

    public static Optional<LocalizedStringEntry> getSearchTerm(final SearchConfig searchConfig, final Map<String, List<String>> queryString,
                                                               final UserContext userContext) {
        return Optional.ofNullable(queryString.get(searchConfig.getSearchTermKey()))
                .map(text -> LocalizedStringEntry.of(userContext.locale(), text.stream().collect(joining(" "))));
    }
}
