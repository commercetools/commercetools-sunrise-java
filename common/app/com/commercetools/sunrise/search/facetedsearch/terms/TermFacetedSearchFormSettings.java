package com.commercetools.sunrise.search.facetedsearch.terms;

import io.sphere.sdk.search.FilterExpression;
import io.sphere.sdk.search.TermFacetedSearchExpression;
import io.sphere.sdk.search.model.FacetedSearchSearchModel;
import io.sphere.sdk.search.model.TermFacetedSearchSearchModel;
import play.mvc.Http;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface TermFacetedSearchFormSettings<T> extends ConfiguredTermFacetedSearchFormSettings, BaseTermFacetedSearchFormSettings<T, String> {

    ConfiguredTermFacetedSearchFormSettings configuration();

    @Override
    default Optional<String> mapFieldValueToValue(final String fieldValue) {
        return Optional.of(fieldValue);
    }

    @Override
    default boolean isValidValue(@Nullable final String value) {
        return value != null && !value.trim().isEmpty();
    }

    @Override
    default List<FilterExpression<T>> buildFilterExpressions(final Http.Context httpContext) {
        final List<String> selectedValues = getAllSelectedValues(httpContext);
        final FacetedSearchSearchModel<T> searchModel = TermFacetedSearchSearchModel.of(getAttributePath());
        final TermFacetedSearchExpression<T> facetedSearchExpr;
        if (selectedValues.isEmpty()) {
            facetedSearchExpr = searchModel.allTerms();
        } else if (isMatchingAll()) {
            facetedSearchExpr = searchModel.containsAll(selectedValues);
        } else {
            facetedSearchExpr = searchModel.containsAny(selectedValues);
        }
        return facetedSearchExpr.filterExpressions();
    }
}
