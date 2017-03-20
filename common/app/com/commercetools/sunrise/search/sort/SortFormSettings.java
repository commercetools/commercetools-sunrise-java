package com.commercetools.sunrise.search.sort;

import com.commercetools.sunrise.framework.viewmodels.forms.FormSettingsWithOptions;
import io.sphere.sdk.queries.QuerySort;
import io.sphere.sdk.search.SortExpression;
import play.mvc.Http;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

public interface SortFormSettings<T> extends FormSettingsWithOptions<SortFormOption, List<String>> {

    ConfiguredSortFormSettings configuration();

    @Override
    default String getFieldName() {
        return configuration().getFieldName();
    }

    default List<SortExpression<T>> buildSearchExpressions(final Http.Context httpContext) {
        return getSelectedOption(httpContext)
                .map(option -> option.getValue().stream()
                    .map(SortExpression::<T>of)
                    .collect(toList()))
                .orElseGet(Collections::emptyList);
    }

    default List<QuerySort<T>> buildQueryExpressions(final Http.Context httpContext) {
        return getSelectedOption(httpContext)
                .map(option -> option.getValue().stream()
                        .map(QuerySort::<T>of)
                        .collect(toList()))
                .orElseGet(Collections::emptyList);
    }

    static <T> SortFormSettings<T> of(final ConfiguredSortFormSettings configuration, final Locale locale) {
        return new SortFormSettingsImpl<T>(configuration, locale);
    }
}
