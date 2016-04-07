package productcatalog.productoverview.search;

import common.contexts.NoLocaleFoundException;
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
    private final SortSelector sortCriteria;
    private final DisplayCriteria displayCriteria;
    private final List<FacetCriteria> facetsCriteria;

    private SearchCriteria(final int page, @Nullable final LocalizedStringEntry searchTerm, final DisplayCriteria displayCriteria,
                           final SortSelector sortCriteria, final List<FacetCriteria> facetsCriteria) {
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

    public SortSelector getSortCriteria() {
        return sortCriteria;
    }

    public List<FacetCriteria> getFacetsCriteria() {
        return facetsCriteria;
    }

    public static SearchCriteria of(final int page, final SearchConfig searchConfig, final Map<String, List<String>> queryString,
                                    final List<Locale> locales, final CategoryTree categoryTree,
                                    final List<Category> selectedCategories) {
        final LocalizedStringEntry searchTerm = getSearchTerm(searchConfig, queryString, locales).orElse(null);
        final DisplayCriteria displayCriteria = DisplayCriteria.of(searchConfig.getDisplayConfig(), queryString);
        final SortSelector sortCriteria = SortSelector.of(searchConfig.getSortConfig(), queryString);
        final List<FacetCriteria> facetsCriteria = searchConfig.getFacetsConfig().stream()
                .map(facetConfig -> FacetCriteria.of(facetConfig, queryString, locales, selectedCategories, categoryTree))
                .collect(toList());
        return new SearchCriteria(page, searchTerm, displayCriteria, sortCriteria, facetsCriteria);
    }

    public static SearchCriteria of(final int page, final SearchConfig searchConfig, final Map<String, List<String>> queryString,
                                    final List<Locale> locales) {
        return of(page, searchConfig, queryString, locales, CategoryTree.of(emptyList()), emptyList());
    }

    public static Optional<LocalizedStringEntry> getSearchTerm(final SearchConfig searchConfig, final Map<String, List<String>> queryString,
                                                               final List<Locale> locales) {
        final Locale locale = locales.stream().findFirst().orElseThrow(NoLocaleFoundException::new);
        return Optional.ofNullable(queryString.get(searchConfig.getSearchTermKey()))
                .map(text -> LocalizedStringEntry.of(locale, text.stream().collect(joining(" "))));
    }
}
