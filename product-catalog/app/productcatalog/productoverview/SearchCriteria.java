package productcatalog.productoverview;

import common.contexts.UserContext;
import common.i18n.I18nIdentifier;
import common.i18n.I18nResolver;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.facets.*;
import io.sphere.sdk.models.LocalizedStringEntry;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionFacetedSearchSearchModel;
import io.sphere.sdk.products.search.ProductProjectionSearchModel;
import io.sphere.sdk.search.FacetedSearchExpression;
import io.sphere.sdk.search.model.TermFacetedSearchSearchModel;
import play.mvc.Http;
import productcatalog.productoverview.search.DisplayCriteria;
import productcatalog.productoverview.search.SearchConfig;
import productcatalog.productoverview.search.SortCriteria;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static common.utils.UrlUtils.getQueryString;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static productcatalog.productoverview.search.SunriseFacetType.*;

public class SearchCriteria {
    private static final ProductProjectionFacetedSearchSearchModel FACET = ProductProjectionSearchModel.of().facetedSearch();

    private final SearchConfig searchConfig;
    private final Map<String, List<String>> queryString;
    private final I18nResolver i18nResolver;
    private final UserContext userContext;
    private final Optional<Category> selectedCategory;
    private final List<String> selectedCategoryIds;
    private final CategoryTree subcategoryTreeFacet;

    private final SortCriteria sortCriteria;
    private final DisplayCriteria displayCriteria;

    private SearchCriteria(final int page, final DisplayCriteria displayCriteria, final SortCriteria sortCriteria,
                           final Map<String, List<String>> queryString, final I18nResolver i18nResolver, final UserContext userContext,
                           final CategoryTree subcategoryTreeFacet, final List<String> selectedCategoryIds,
                           @Nullable final Category selectedCategory, final SearchConfig searchConfig) {
        this.searchConfig = searchConfig;
        this.displayCriteria = displayCriteria;
        this.sortCriteria = sortCriteria;
        this.queryString = queryString;
        this.i18nResolver = i18nResolver;
        this.userContext = userContext;

        this.selectedCategory = Optional.ofNullable(selectedCategory);
        this.selectedCategoryIds = selectedCategoryIds;
        this.subcategoryTreeFacet = subcategoryTreeFacet;
    }

    public DisplayCriteria getDisplayCriteria() {
        return displayCriteria;
    }

    public SortCriteria getSortCriteria() {
        return sortCriteria;
    }

    public List<Facet<ProductProjection>> boundFacets() {
        return categoryFacet()
                .map(categoryFacet -> asList(categoryFacet, colorFacet(), sizeFacet(), brandFacet()))
                .orElseGet(() -> asList(colorFacet(), sizeFacet(), brandFacet()));
    }

    public List<FacetedSearchExpression<ProductProjection>> selectedFacets() {
        return boundFacets().stream()
                .map(Facet::getFacetedSearchExpression)
                .collect(toList());
    }



    public Optional<LocalizedStringEntry> searchTerm() {
        return Optional.ofNullable(queryString.get(searchConfig.getSearchTermKey()))
                .map(text -> LocalizedStringEntry.of(userContext.locale(), text.stream().collect(joining(" "))));
    }

    public static SearchCriteria of(final int page, final SearchConfig searchConfig, final Http.Request request,
                                    final I18nResolver i18nResolver, final UserContext userContext,
                                    final CategoryTree subcategoryTreeFacet, final List<String> selectedCategoryIds,
                                    final Category selectedCategory) {
        final Map<String, List<String>> queryString = getQueryString(request);
        final DisplayCriteria displayCriteria = DisplayCriteria.of(searchConfig.getDisplayConfig(), queryString, userContext, i18nResolver);
        final SortCriteria sortCriteria = SortCriteria.of(searchConfig.getSortConfig(), queryString, userContext, i18nResolver);
        return new SearchCriteria(page, displayCriteria, sortCriteria, queryString, i18nResolver, userContext, subcategoryTreeFacet, selectedCategoryIds, selectedCategory, searchConfig);
    }

    public static SearchCriteria of(final int page, final SearchConfig configuration, final Http.Request request,
                                    final I18nResolver i18nResolver, final UserContext userContext,
                                    final CategoryTree categoryTree) {
        return of(page, configuration, request, i18nResolver, userContext, categoryTree, emptyList(), null);
    }

    private Facet<ProductProjection> brandFacet() {
        final String key = "brands";

        final String label = i18nResolver.getOrEmpty(userContext.locales(), I18nIdentifier.of("catalog:filters.brand"));
        return SelectFacetBuilder.of(key, label, FACET.allVariants().attribute().ofEnum("designer").label())
                .selectedValues(queryString.getOrDefault(key, emptyList()))
                .type(SELECT_LIST_DISPLAY)
                .countHidden(true)
                .build();
    }

    private Facet<ProductProjection> sizeFacet() {
        final String key = "size";
        final String label = i18nResolver.getOrEmpty(userContext.locales(), I18nIdentifier.of("catalog:filters.size"));
        return SelectFacetBuilder.of(key, label, FACET.allVariants().attribute().ofEnum("commonSize").label())
                .mapper(SortedFacetOptionMapper.of(searchConfig.getSortedSizes()))
                .selectedValues(queryString.getOrDefault(key, emptyList()))
                .type(SELECT_TWO_COLUMNS_DISPLAY)
                .countHidden(true)
                .build();
    }

    private Facet<ProductProjection> colorFacet() {
        final String key = "color";
        final String label = i18nResolver.getOrEmpty(userContext.locales(), I18nIdentifier.of("catalog:filters.color"));
        return SelectFacetBuilder.of(key, label, FACET.allVariants().attribute().ofLocalizedEnum("color").label().locale(userContext.locale()))
                .mapper(AlphabeticallySortedFacetOptionMapper.of())
                .selectedValues(queryString.getOrDefault(key, emptyList()))
                .type(SELECT_TWO_COLUMNS_DISPLAY)
                .countHidden(true)
                .build();
    }

    private Optional<Facet<ProductProjection>> categoryFacet() {
        return selectedCategory.map(category -> {
            final String key = "productType";
            final String label = i18nResolver.getOrEmpty(userContext.locales(), I18nIdentifier.of("catalog:filters.productType"));
            final TermFacetedSearchSearchModel<ProductProjection> model = TermFacetedSearchSearchModel.of("variants.categories.id");
            return SelectFacetBuilder.of(key, label, model)
                    .mapper(HierarchicalCategoryFacetOptionMapper.of(singletonList(category), subcategoryTreeFacet, userContext.locales()))
                    .selectedValues(selectedCategoryIds)
                    .type(SELECT_CATEGORY_HIERARCHICAL_DISPLAY)
                    .countHidden(true)
                    .multiSelect(false)
                    .build();
        });
    }
}
