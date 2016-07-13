package com.commercetools.sunrise.productcatalog.productoverview.search.sort;

import com.commercetools.sunrise.common.contexts.RequestContext;
import com.commercetools.sunrise.common.contexts.UserContext;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.SortExpression;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static com.commercetools.sunrise.productcatalog.productoverview.search.SearchUtils.localizeExpression;
import static com.commercetools.sunrise.productcatalog.productoverview.search.SearchUtils.selectedValues;

public class SortSelectorFactory {

    @Inject
    private UserContext userContext;
    @Inject
    private SortConfig config;
    @Inject
    private RequestContext requestContext;

    public SortSelector create() {
        final List<SortOption> options = localizedSortOptions();
        final List<SortOption> defaultOptions = options.stream().filter(SortOption::isDefault).collect(toList());
        final List<SortOption> selectedOptions = findSelectedOptions(options, selectedValues(config.getKey(), requestContext));
        return SortSelector.of(config.getKey(), options, defaultOptions, selectedOptions);
    }

    protected List<SortOption> localizedSortOptions() {
        return config.getOptions().stream()
                .map(option -> {
                    final List<SortExpression<ProductProjection>> localizedExprList = option.getExpressions().stream()
                            .map(expr -> SortExpression.<ProductProjection>of(localizeExpression(expr.expression(), userContext.locale())))
                            .collect(toList());
                    return option.withExpressions(localizedExprList);
                })
                .collect(toList());
    }


    protected List<SortOption> findSelectedOptions(final List<SortOption> options, final List<String> selectedValues) {
        return selectedValues.stream()
                .map(value -> findOptionByValue(value, options).orElse(null))
                .filter(value -> value != null)
                .collect(toList());
    }

    protected Optional<SortOption> findOptionByValue(final String value, final List<SortOption> options) {
        return options.stream()
                .filter(option -> option.getValue().equals(value))
                .findFirst();
    }
}
