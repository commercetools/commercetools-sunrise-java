package com.commercetools.sunrise.productcatalog.productoverview.search.sort;

import com.commercetools.sunrise.common.SunriseConfigurationException;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.SortExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public final class SortConfigProvider implements Provider<SortConfig> {
    private static final Logger logger = LoggerFactory.getLogger(SortConfigProvider.class);

    private static final String CONFIG_KEY = "pop.sortProducts.key";
    private static final String CONFIG_OPTIONS = "pop.sortProducts.options";

    private static final String OPTION_LABEL_ATTR = "label";
    private static final String OPTION_VALUE_ATTR = "value";
    private static final String OPTION_EXPR_ATTR = "expr";
    private static final String OPTION_DEFAULT_ATTR = "default";

    @Inject
    private Configuration configuration;

    @Override
    public SortConfig get() {
        final String key = configuration.getString(CONFIG_KEY, "sort");
        final List<SortOption> options = getOptions(configuration);
        logger.debug("Provide SortConfig: {}", options.stream().map(SortOption::getValue).collect(toList()));
        return SortConfig.of(key, options);
    }

    private static List<SortOption> getOptions(final Configuration configuration) {
        return configuration.getConfigList(CONFIG_OPTIONS, emptyList()).stream()
                .map(SortConfigProvider::initializeOption)
                .collect(toList());
    }

    private static SortOption initializeOption(final Configuration optionConfig) {
        final String label = optionConfig.getString(OPTION_LABEL_ATTR, "");
        final String value = getValue(optionConfig);
        final List<SortExpression<ProductProjection>> expressions = getExpressions(optionConfig);
        final boolean isDefault = optionConfig.getBoolean(OPTION_DEFAULT_ATTR, false);
        return SortOption.of(value, label, expressions, isDefault);
    }

    private static String getValue(final Configuration optionConfig) {
        return Optional.ofNullable(optionConfig.getString(OPTION_VALUE_ATTR))
                .orElseThrow(() -> new SunriseConfigurationException("Missing sort value", OPTION_VALUE_ATTR, CONFIG_OPTIONS));
    }

    private static List<SortExpression<ProductProjection>> getExpressions(final Configuration optionConfig) {
        final List<SortExpression<ProductProjection>> expressions = optionConfig.getStringList(OPTION_EXPR_ATTR, emptyList()).stream()
                .map(SortExpression::<ProductProjection>of)
                .collect(toList());
        if (expressions.isEmpty()) {
            throw new SunriseConfigurationException("Missing sort expression", OPTION_EXPR_ATTR, CONFIG_OPTIONS);
        }
        return expressions;
    }
}
