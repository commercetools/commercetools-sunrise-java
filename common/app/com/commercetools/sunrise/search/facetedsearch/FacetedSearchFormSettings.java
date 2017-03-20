package com.commercetools.sunrise.search.facetedsearch;

import io.sphere.sdk.search.FacetExpression;
import io.sphere.sdk.search.FacetedSearchExpression;
import io.sphere.sdk.search.FilterExpression;
import play.mvc.Http;

import java.util.List;

public interface FacetedSearchFormSettings<T> extends BaseFacetedSearchFormSettings {

    FacetExpression<T> buildFacetExpression();

    List<FilterExpression<T>> buildFilterExpressions(Http.Context httpContext);

    FacetedSearchExpression<T> buildFacetedSearchExpression(Http.Context httpContext);
}
