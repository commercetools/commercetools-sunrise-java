package com.commercetools.sunrise.search.facetedsearch.sliderranges;

import com.commercetools.sunrise.search.facetedsearch.FacetedSearchFormSettings;
import io.sphere.sdk.search.FilterExpression;
import io.sphere.sdk.search.PagedSearchResult;
import io.sphere.sdk.search.RangeFacetExpression;
import io.sphere.sdk.search.RangeFacetedSearchExpression;
import io.sphere.sdk.search.model.RangeTermFacetSearchModel;
import io.sphere.sdk.search.model.RangeTermFacetedSearchSearchModel;
import io.sphere.sdk.search.model.SimpleRangeStats;
import io.sphere.sdk.search.model.TypeSerializer;
import play.mvc.Http;

import java.util.List;
import java.util.Optional;

import static com.commercetools.sunrise.search.facetedsearch.RangeUtils.parseFilterRange;

public interface SliderRangeFacetedSearchFormSettings<T> extends ConfiguredSliderRangeFacetedSearchFormSettings, FacetedSearchFormSettings<T> {

    ConfiguredSliderRangeFacetedSearchFormSettings configuration();

    @Override
    default RangeFacetedSearchExpression<T> buildFacetedSearchExpression(final Http.Context httpContext) {
        final RangeFacetExpression<T> facetExpression = buildFacetExpression();
        final List<FilterExpression<T>> filterExpressions = buildFilterExpressions(httpContext);
        return RangeFacetedSearchExpression.of(facetExpression, filterExpressions);
    }

    @Override
    default RangeFacetExpression<T> buildFacetExpression() {
        return RangeTermFacetSearchModel.<T, String>of(getAttributePath(), TypeSerializer.ofString())
                .withCountingProducts(isCountDisplayed())
                .allRanges();
    }

    @Override
    default List<FilterExpression<T>> buildFilterExpressions(final Http.Context httpContext) {
        final RangeTermFacetedSearchSearchModel<T> searchModel = RangeTermFacetedSearchSearchModel.of(getAttributePath());
        return parseFilterRange(
                getLowerEndpointSettings().getSelectedValueOrDefault(httpContext),
                getUpperEndpointSettings().getSelectedValueOrDefault(httpContext))
                .map(searchModel::isBetween)
                .orElseGet(searchModel::allRanges).filterExpressions();
    }

    default Optional<SimpleRangeStats> findFacetResult(final PagedSearchResult<T> pagedSearchResult) {
        final RangeFacetExpression<T> facetExpression = buildFacetExpression();
        return Optional.ofNullable(pagedSearchResult.getRangeStatsOfAllRanges(facetExpression));
    }
}
