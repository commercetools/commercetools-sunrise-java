package com.commercetools.sunrise.search.facetedsearch;

import io.sphere.sdk.search.FacetedSearchExpression;
import play.mvc.Http;

import java.util.List;

import static java.util.stream.Collectors.toList;

public interface FacetedSearchFormSettingsList<T> {

    List<? extends FacetedSearchFormSettings<T>> getSettings();

    default List<FacetedSearchExpression<T>> buildFacetedSearchExpressions(final Http.Context httpContext) {
        return getSettings().stream()
                .map(settings -> settings.buildFacetedSearchExpression(httpContext))
                .collect(toList());
    }

    static <T> FacetedSearchFormSettingsList<T> of(final List<? extends FacetedSearchFormSettings<T>> settings) {
        return new FacetedSearchFormSettingsListImpl<>(settings);
    }
}
