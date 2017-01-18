package com.commercetools.sunrise.productcatalog.productoverview.search.sort;

import com.commercetools.sunrise.common.contexts.RequestContext;
import com.commercetools.sunrise.common.contexts.RequestScoped;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.SortExpression;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import static com.commercetools.sunrise.productcatalog.productoverview.search.SearchUtils.localizeExpression;
import static com.commercetools.sunrise.productcatalog.productoverview.search.SearchUtils.selectedValues;
import static java.util.stream.Collectors.toList;

@RequestScoped
public class SortSelectorFactory {

    private final Locale locale;
    private final SortConfig config;
    private final RequestContext requestContext;

    @Inject
    public SortSelectorFactory(final Locale locale, final SortConfig config, final RequestContext requestContext) {
        this.locale = locale;
        this.config = config;
        this.requestContext = requestContext;
    }

    public SortSelector create() {
        final List<SortOption> options = localizedSortOptions();
        final List<SortOption> defaultOptions = options.stream().filter(SortOption::isDefault).collect(toList());
        final List<SortOption> selectedOptions = findSelectedOptions(options, selectedValues(config.getKey(), requestContext));
        return SortSelector.of(config.getKey(), options, defaultOptions, selectedOptions);
    }

    private List<SortOption> localizedSortOptions() {
        return config.getOptions().stream()
                .map(option -> {
                    final List<SortExpression<ProductProjection>> localizedExprList = option.getExpressions().stream()
                            .map(expr -> SortExpression.<ProductProjection>of(localizeExpression(expr.expression(), locale)))
                            .collect(toList());
                    return option.withExpressions(localizedExprList);
                })
                .collect(toList());
    }


    private List<SortOption> findSelectedOptions(final List<SortOption> options, final List<String> selectedValues) {
        return selectedValues.stream()
                .map(value -> findOptionByValue(value, options).orElse(null))
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private Optional<SortOption> findOptionByValue(final String value, final List<SortOption> options) {
        return options.stream()
                .filter(option -> option.getValue().equals(value))
                .findFirst();
    }
}
