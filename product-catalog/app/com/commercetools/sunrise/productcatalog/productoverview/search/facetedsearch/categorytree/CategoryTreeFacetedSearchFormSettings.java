package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.categorytree;

import com.commercetools.sunrise.search.facetedsearch.terms.BaseTermFacetedSearchFormSettings;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.FilterExpression;
import play.mvc.Http;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public interface CategoryTreeFacetedSearchFormSettings extends ConfiguredCategoryTreeFacetedSearchFormSettings, BaseTermFacetedSearchFormSettings<ProductProjection, Category> {

    ConfiguredCategoryTreeFacetedSearchFormSettings configuration();

    /**
     * Maps the identifier of the category to its category ID, which is ultimately used for building filter expressions.
     * @param fieldValue the identifier of the category as it is received in the HTTP request
     * @return the ID of the category identified by that value
     */
    @Override
    Optional<Category> mapFieldValueToValue(final String fieldValue);

    @Override
    default boolean isValidValue(@Nullable final Category category) {
        return category != null;
    }

    @Override
    default List<String> getSelectedValuesAsRawList(final Http.Context httpContext) {
        return Optional.ofNullable(httpContext.args.get("ROUTE_PATTERN"))
                .map(routePattern -> routePattern.toString().replaceAll("<[^>]+>", "")) // remove regex since splitting '$categoryIdentifier<[^/]+>' with '/' would create more words
                .map(routePattern -> {
                    final List<String> paths = asList(routePattern.split("/"));
                    return paths.indexOf("$categoryIdentifier");
                })
                .filter(index -> index >= 0)
                .map(index -> httpContext.request().path().split("/")[index])
                .map(Collections::singletonList)
                .orElseGet(Collections::emptyList);
    }

    @Override
    default List<FilterExpression<ProductProjection>> buildFilterExpressions(final Http.Context httpContext) {
        return getSelectedValue(httpContext)
                .map(category -> {
                    final String expression = String.format("%s: subtree(\"%s\")", getAttributePath(), category.getId());
                    return singletonList(FilterExpression.<ProductProjection>of(expression));
                })
                .orElseGet(Collections::<FilterExpression<ProductProjection>>emptyList);
    }
}
