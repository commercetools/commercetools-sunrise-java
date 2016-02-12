package productcatalog.productoverview;

import common.contexts.UserContext;
import common.i18n.I18nResolver;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.facets.*;
import io.sphere.sdk.models.LocalizedStringEntry;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionFacetedSearchSearchModel;
import io.sphere.sdk.products.search.ProductProjectionSearchModel;
import io.sphere.sdk.products.search.ProductProjectionSortSearchModel;
import io.sphere.sdk.search.FacetedSearchExpression;
import io.sphere.sdk.search.SortExpression;
import io.sphere.sdk.search.model.TermFacetedSearchSearchModel;
import play.Configuration;
import play.mvc.Http;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static productcatalog.productoverview.SunriseFacetType.*;

public class SearchCriteria {
    private static final ProductProjectionSortSearchModel SORT = ProductProjectionSearchModel.of().sort();
    private static final ProductProjectionFacetedSearchSearchModel FACET = ProductProjectionSearchModel.of().facetedSearch();
    private static final SortExpression<ProductProjection> DEFAULT_SORT = SORT.createdAt().byDesc();
    private final Http.Request request;
    private final I18nResolver i18nResolver;
    private final UserContext userContext;
    private final Optional<Category> selectedCategory;
    private final List<String> selectedCategoryIds;
    private final CategoryTree subcategoryTreeFacet;
    private final List<String> sortedSizes;
    private final String fulltextKey;
    private final String sortKey;
    private final String displayKey;
    private final List<Integer> pageSizeOptions;
    private final int pageSizeDefault;

    private SearchCriteria(final Configuration configuration, final Http.Request request, final I18nResolver i18nResolver,
                           final UserContext userContext, @Nullable final Category selectedCategory,
                           final List<String> selectedCategoryIds, final CategoryTree subcategoryTreeFacet) {
        this.request = request;
        this.i18nResolver = i18nResolver;
        this.userContext = userContext;
        this.selectedCategory = Optional.ofNullable(selectedCategory);
        this.selectedCategoryIds = selectedCategoryIds;
        this.subcategoryTreeFacet = subcategoryTreeFacet;
        this.sortedSizes = configuration.getStringList("pop.facet.size", emptyList());
        this.fulltextKey = configuration.getString("pop.fulltext.key", "q");
        this.sortKey = configuration.getString("pop.sort.key", "sort");
        this.displayKey = configuration.getString("pop.pageSize.key", "display");
        this.pageSizeOptions = configuration.getIntList("pop.pageSize.options", asList(24, 48));
        this.pageSizeDefault = configuration.getInt("pop.pageSize.default", 24);
        // TODO Move more keys/values to configuration
    }

    public List<Facet<ProductProjection>> boundFacets() {
        return categoryFacet()
                .map(categoryFacet -> asList(categoryFacet, colorFacet(), sizeFacet(), brandFacet()))
                .orElseGet(() -> asList(colorFacet(), sizeFacet(), brandFacet()));
    }

    public SortSelector boundSortSelector() {
        return new SortSelector(sortKey, asList(newestSortOption(), priceAscSortOption(), priceDescSortOption()));
    }

    public DisplaySelector boundDisplaySelector() {
        return new DisplaySelector(displayKey, pageSizeOptions, selectedDisplay());
    }

    public List<FacetedSearchExpression<ProductProjection>> selectedFacets() {
        return boundFacets().stream()
                .map(Facet::getFacetedSearchExpression)
                .collect(toList());
    }

    public int selectedDisplay() {
        return pageSizeOptions.stream()
                .filter(option -> isSelected(displayKey, option.toString()))
                .findFirst()
                .orElse(pageSizeDefault);
    }

    public List<SortExpression<ProductProjection>> selectedSort() {
        return boundSortSelector().getList().stream()
                .filter(SortOption::isSelected)
                .map(SortOption::getSortExpressions)
                .findFirst()
                .orElseGet(Collections::emptyList);
    }

    public Optional<LocalizedStringEntry> searchTerm() {
        return Optional.ofNullable(request.getQueryString(fulltextKey))
                .map(text -> LocalizedStringEntry.of(userContext.locale(), text));
    }

    public static SearchCriteria of(final Configuration configuration, final Http.Request request,
                                    final I18nResolver i18nResolver, final UserContext userContext,
                                    final Category selectedCategory, final List<String> selectedCategoryIds,
                                    final CategoryTree subcategoryTreeFacet) {
        return new SearchCriteria(configuration, request, i18nResolver, userContext, selectedCategory, selectedCategoryIds, subcategoryTreeFacet);
    }

    public static SearchCriteria of(final Configuration configuration, final Http.Request request,
                                    final I18nResolver i18nResolver, final UserContext userContext,
                                      final CategoryTree categoryTree) {
        return of(configuration, request, i18nResolver, userContext, null, emptyList(), categoryTree);
    }

    private Facet<ProductProjection> brandFacet() {
        final String key = "brands";
        final String label = i18nResolver.getOrEmpty(userContext.locales(), "catalog", "filters.brand");
        return SelectFacetBuilder.of(key, label, FACET.allVariants().attribute().ofEnum("designer").label())
                .selectedValues(getSelectedValues(key))
                .type(SELECT_LIST_DISPLAY)
                .countHidden(true)
                .build();
    }

    private Facet<ProductProjection> sizeFacet() {
        final String key = "size";
        final String label = i18nResolver.getOrEmpty(userContext.locales(), "catalog", "filters.size");
        return SelectFacetBuilder.of(key, label, FACET.allVariants().attribute().ofEnum("commonSize").label())
                .mapper(SortedFacetOptionMapper.of(sortedSizes))
                .selectedValues(getSelectedValues(key))
                .type(SELECT_TWO_COLUMNS_DISPLAY)
                .countHidden(true)
                .build();
    }

    private Facet<ProductProjection> colorFacet() {
        final String key = "color";
        final String label = i18nResolver.getOrEmpty(userContext.locales(), "catalog", "filters.color");
        return SelectFacetBuilder.of(key, label, FACET.allVariants().attribute().ofLocalizedEnum("color").label().locale(userContext.locale()))
                .mapper(AlphabeticallySortedFacetOptionMapper.of())
                .selectedValues(getSelectedValues(key))
                .type(SELECT_TWO_COLUMNS_DISPLAY)
                .countHidden(true)
                .build();
    }

    private Optional<Facet<ProductProjection>> categoryFacet() {
        return selectedCategory.map(category -> {
            final String key = "productType";
            final String label = i18nResolver.getOrEmpty(userContext.locales(), "catalog", "filters.productType");
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

    private SortOption<ProductProjection> newestSortOption() {
        final String value = "new";
        final String label = i18nResolver.getOrEmpty(userContext.locales(), "catalog", "sortSelector.new");
        return sortOption(value, label, SORT.createdAt().desc());
    }

    private SortOption<ProductProjection> priceAscSortOption() {
        final String value = "price-asc";
        final String label = i18nResolver.getOrEmpty(userContext.locales(), "catalog", "sortSelector.priceAsc");
        return sortOption(value, label, SORT.allVariants().price().asc());
    }

    private SortOption<ProductProjection> priceDescSortOption() {
        final String value = "price-desc";
        final String label = i18nResolver.getOrEmpty(userContext.locales(), "catalog", "sortSelector.priceDesc");
        return sortOption(value, label, SORT.allVariants().price().desc());
    }

    private SortOption<ProductProjection> sortOption(final String value, final String label, final SortExpression<ProductProjection> sortExpression) {
        final List<SortExpression<ProductProjection>> sortExpressionList = new ArrayList<>();
        sortExpressionList.add(sortExpression);
        if (!DEFAULT_SORT.equals(sortExpression)) {
            sortExpressionList.add(DEFAULT_SORT);
        }
        final SortOption<ProductProjection> option = new SortOption<>(label, value, sortExpressionList);
        if (isSelected(sortKey, value)) {
            option.setSelected(true);
        }
        return option;
    }

    private boolean isSelected(final String key, final String value) {
        return value.equals(request.getQueryString(key));
    }

    private List<String> getSelectedValues(final String key) {
        return asList(request.queryString().getOrDefault(key, new String[0]));
    }
}
