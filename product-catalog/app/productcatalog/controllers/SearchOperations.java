package productcatalog.controllers;

import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.facets.*;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionFacetAndFilterSearchModel;
import io.sphere.sdk.products.search.ProductProjectionSearchModel;
import io.sphere.sdk.products.search.ProductProjectionSortSearchModel;
import io.sphere.sdk.search.SortExpression;
import io.sphere.sdk.search.model.TermFacetAndFilterSearchModel;
import play.Configuration;
import play.i18n.Messages;
import play.mvc.Http;
import productcatalog.models.SortOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static productcatalog.models.SunriseFacetType.*;

public class SearchOperations {
    private static final ProductProjectionSortSearchModel SORT = ProductProjectionSearchModel.of().sort();
    private static final ProductProjectionFacetAndFilterSearchModel FACET = ProductProjectionSearchModel.of().facetedSearch();
    private static final SortExpression<ProductProjection> DEFAULT_SORT = SORT.createdAt().byDesc();
    private static final String SORT_KEY = "sort";
    private final Http.Request request;
    private final Messages messages;
    private final Locale locale;
    private final List<String> sortedSizes;

    public SearchOperations(final Configuration configuration, final Http.Request request, final Messages messages, final Locale locale) {
        this.request = request;
        this.messages = messages;
        this.locale = locale;
        this.sortedSizes = configuration.getStringList("pop.facet.size", emptyList());
    }

    public List<Facet<ProductProjection>> boundFacets(final CategoryTree subcategoryTree) {
        return asList(categoryFacet(subcategoryTree), colorFacet(), sizeFacet(), brandFacet());
    }

    public List<SortOption<ProductProjection>> boundSortOptions() {
        return asList(newestSortOption(), priceAscSortOption(), priceDescSortOption());
    }

    private Facet<ProductProjection> brandFacet() {
        final String key = "brands";
        return SelectFacetBuilder.of(key, messages.at("pop.facet.brand"), FACET.allVariants().attribute().ofEnum("designer").label())
                .selectedValues(getSelectedValues(key))
                .type(SELECT_LIST_DISPLAY)
                .countHidden(true)
                .build();
    }

    private Facet<ProductProjection> sizeFacet() {
        final String key = "size";
        return SelectFacetBuilder.of(key, messages.at("pop.facet.size"), FACET.allVariants().attribute().ofEnum("commonSize").label())
                .mapper(SortedFacetOptionMapper.of(sortedSizes))
                .selectedValues(getSelectedValues(key))
                .type(SELECT_TWO_COLUMNS_DISPLAY)
                .countHidden(true)
                .build();
    }

    private Facet<ProductProjection> colorFacet() {
        final String key = "color";
        return SelectFacetBuilder.of(key, messages.at("pop.facet.color"), FACET.allVariants().attribute().ofLocalizableEnum("color").label().locale(locale))
                .mapper(AlphabeticallySortedFacetOptionMapper.of())
                .selectedValues(getSelectedValues(key))
                .type(SELECT_TWO_COLUMNS_DISPLAY)
                .countHidden(true)
                .build();
    }

    private Facet<ProductProjection> categoryFacet(final CategoryTree subcategoryTree) {
        final TermFacetAndFilterSearchModel<ProductProjection> model = TermFacetAndFilterSearchModel.of("variants.categories.id");
        return SelectFacetBuilder.of("", messages.at("pop.facet.productType"), model)
                .mapper(HierarchicalCategoryFacetOptionMapper.of(subcategoryTree, singletonList(locale)))
                .type(SELECT_CATEGORY_HIERARCHICAL_DISPLAY)
                .countHidden(false)
                .multiSelect(false)
                .build();
    }

    private SortOption<ProductProjection> newestSortOption() {
        final String value = "";
        return sortOption(value, messages.at("pop.sort.new"), SORT.createdAt().byDesc());
    }

    private SortOption<ProductProjection> priceAscSortOption() {
        final String value = "price-asc";
        return sortOption(value, messages.at("pop.sort.priceAsc"), SORT.allVariants().price().byAsc());
    }

    private SortOption<ProductProjection> priceDescSortOption() {
        final String value = "price-desc";
        return sortOption(value, messages.at("pop.sort.priceDesc"), SORT.allVariants().price().byDesc());
    }

    private SortOption<ProductProjection> sortOption(final String value, final String label, final SortExpression<ProductProjection> sortExpression) {
        final List<SortExpression<ProductProjection>> sortExpressionList = new ArrayList<>();
        sortExpressionList.add(sortExpression);
        if (!DEFAULT_SORT.equals(sortExpression)) {
            sortExpressionList.add(DEFAULT_SORT);
        }
        final SortOption<ProductProjection> option = new SortOption<>(label, value, sortExpressionList);
        if (isSelected(SORT_KEY, value)) {
            option.setSelected(true);
        }
        return option;
    }

    private boolean isSelected(final String key, final String value) {
        return key.equals(request.getQueryString(value));
    }

    private List<String> getSelectedValues(final String key) {
        return asList(request.queryString().getOrDefault(key, new String[0]));
    }
}
