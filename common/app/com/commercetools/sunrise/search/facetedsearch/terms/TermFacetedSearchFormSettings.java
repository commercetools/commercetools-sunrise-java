package com.commercetools.sunrise.search.facetedsearch.terms;

import com.commercetools.sunrise.framework.viewmodels.forms.FormSettings;
import com.commercetools.sunrise.search.facetedsearch.FacetedSearchFormSettings;
import io.sphere.sdk.search.*;
import io.sphere.sdk.search.model.FacetedSearchSearchModel;
import io.sphere.sdk.search.model.TermFacetSearchModel;
import io.sphere.sdk.search.model.TermFacetedSearchSearchModel;
import io.sphere.sdk.search.model.TypeSerializer;
import play.mvc.Http;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface TermFacetedSearchFormSettings<T> extends ConfiguredTermFacetedSearchFormSettings, FacetedSearchFormSettings<T>, FormSettings<String> {

    ConfiguredTermFacetedSearchFormSettings configuration();

    @Override
    default String getDefaultValue() {
        return "";
    }

    @Nullable
    @Override
    default String mapFieldValueToValue(final String fieldValue) {
        return fieldValue;
    }

    @Override
    default boolean isValidValue(@Nullable final String value) {
        return value != null;
    }

    @Override
    default TermFacetedSearchExpression<T> buildFacetedSearchExpression(final Http.Context httpContext) {
        final TermFacetExpression<T> facetExpression = buildFacetExpression();
        final List<FilterExpression<T>> filterExpressions = buildFilterExpressions(httpContext);
        return TermFacetedSearchExpression.of(facetExpression, filterExpressions);
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

    @Override
    default TermFacetExpression<T> buildFacetExpression() {
        final TermFacetSearchModel<T, String> searchModel = TermFacetSearchModel.<T, String>of(getAttributePath(), TypeSerializer.ofString());
        return searchModel
                .withCountingProducts(isCountDisplayed())
                .allTerms();
    }

    default Optional<TermFacetResult> findFacetResult(final PagedSearchResult<T> pagedSearchResult) {
        final TermFacetExpression<T> facetExpression = buildFacetExpression();
        return Optional.ofNullable(pagedSearchResult.getFacetResult(facetExpression));
    }
}
