package com.commercetools.sunrise.search.facetedsearch.terms;

import com.commercetools.sunrise.framework.viewmodels.forms.FormSettings;
import com.commercetools.sunrise.search.facetedsearch.FacetedSearchFormSettings;
import io.sphere.sdk.search.*;
import io.sphere.sdk.search.model.TermFacetSearchModel;
import io.sphere.sdk.search.model.TypeSerializer;
import play.mvc.Http;

import java.util.List;
import java.util.Optional;

public interface BaseTermFacetedSearchFormSettings<T, V> extends ConfiguredTermFacetedSearchFormSettings, FacetedSearchFormSettings<T>, FormSettings<V> {

    ConfiguredTermFacetedSearchFormSettings configuration();

    @Override
    default TermFacetedSearchExpression<T> buildFacetedSearchExpression(final Http.Context httpContext) {
        final TermFacetExpression<T> facetExpression = buildFacetExpression();
        final List<FilterExpression<T>> filterExpressions = buildFilterExpressions(httpContext);
        return TermFacetedSearchExpression.of(facetExpression, filterExpressions);
    }

    @Override
    default TermFacetExpression<T> buildFacetExpression() {
        final TermFacetSearchModel<T, String> searchModel = TermFacetSearchModel.of(getAttributePath(), TypeSerializer.ofString());
        return searchModel
                .withCountingProducts(isCountDisplayed())
                .allTerms();
    }

    default Optional<TermFacetResult> findFacetResult(final PagedSearchResult<T> pagedSearchResult) {
        final TermFacetExpression<T> facetExpression = buildFacetExpression();
        return Optional.ofNullable(pagedSearchResult.getFacetResult(facetExpression));
    }
}
