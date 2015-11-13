package productcatalog.controllers;

import io.sphere.sdk.categories.Category;
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
import productcatalog.models.SortOptionImpl;

import java.util.List;
import java.util.Locale;

import static io.sphere.sdk.facets.DefaultFacetType.HIERARCHICAL_SELECT;
import static io.sphere.sdk.facets.DefaultFacetType.SORTED_SELECT;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class SearchOperations {
    private static final ProductProjectionSortSearchModel SORT = ProductProjectionSearchModel.of().sort();
    private static final ProductProjectionFacetAndFilterSearchModel FACET = ProductProjectionSearchModel.of().facetedSearch();
    private final Http.Request request;
    private final Messages messages;
    private final Locale locale;
    private final List<String> sortedSizes;

    public SearchOperations(final Configuration configuration, final Http.Request request, final Messages messages, final Locale locale) {
        this.request = request;
        this.messages = messages;
        this.locale = locale;
        this.sortedSizes = configuration.getStringList("pop.sizeFacet");
    }

    public List<Facet<ProductProjection>> boundFacets(final List<Category> categories) {
        return asList(categoryFacet(categories), colorFacet(), sizeFacet(), brandFacet());
    }

    public List<SortOption<ProductProjection>> boundSortOptions() {
        final String key = "sort";
        final SortExpression<ProductProjection> newestFirst = SORT.createdAt().byDesc();
        return asList(newestSortOption(key, newestFirst), priceAscSortOption(key, newestFirst), priceDescSortOption(key, newestFirst));
    }

    private Facet<ProductProjection> brandFacet() {
        final String key = "brands";
        return SelectFacetBuilder.of(key, messages.at("pop.facetBrand"), FACET.allVariants().attribute().ofEnum("designer").label())
                .selectedValues(getSelectedValues(key))
                .countHidden(true)
                .build();
    }

    private Facet<ProductProjection> sizeFacet() {
        final String key = "size";
        return SelectFacetBuilder.of(key, messages.at("pop.facetSize"), FACET.allVariants().attribute().ofEnum("commonSize").label())
                .mapper(SortedFacetOptionMapper.of(sortedSizes))
                .selectedValues(getSelectedValues(key))
                .type(SORTED_SELECT)
                .countHidden(true)
                .build();
    }

    private Facet<ProductProjection> colorFacet() {
        final String key = "color";
        return SelectFacetBuilder.of(key, messages.at("pop.facetColor"), FACET.allVariants().attribute().ofLocalizableEnum("color").label().locale(locale))
                .selectedValues(getSelectedValues(key))
                .countHidden(true)
                .build();
    }

    private Facet<ProductProjection> categoryFacet(final List<Category> categories) {
        final String key = "product-type";
        final TermFacetAndFilterSearchModel<ProductProjection> model = TermFacetAndFilterSearchModel.of("variants.categories.id");
        return SelectFacetBuilder.of(key, messages.at("pop.facetProductType"), model)
                .selectedValues(getSelectedValues(key))
                .mapper(HierarchicalCategoryFacetOptionMapper.of(categories, singletonList(locale)))
                .type(HIERARCHICAL_SELECT)
                .countHidden(true)
                .multiSelect(false)
                .build();
    }

    private SortOption<ProductProjection> newestSortOption(final String key, final SortExpression<ProductProjection> newestFirst) {
        final String value = "";
        return SortOptionImpl.of(value, messages.at("pop.sortNew"), isSelected(key, value), singletonList(newestFirst));
    }

    private SortOption<ProductProjection> priceAscSortOption(final String key, final SortExpression<ProductProjection> newestFirst) {
        final String value = "price-asc";
        return SortOptionImpl.of(value, messages.at("pop.sortPriceAsc"), isSelected(key, value), asList(newestFirst, SORT.allVariants().price().byAsc()));
    }

    private SortOption<ProductProjection> priceDescSortOption(final String key, final SortExpression<ProductProjection> newestFirst) {
        final String value = "price-desc";
        return SortOptionImpl.of(value, messages.at("pop.sortPriceDesc"), isSelected(key, value), asList(newestFirst, SORT.allVariants().price().byDesc()));
    }

    private boolean isSelected(final String key, final String value) {
        return key.equals(request.getQueryString(value));
    }

    private List<String> getSelectedValues(final String key) {
        return asList(request.queryString().getOrDefault(key, new String[0]));
    }
}
