package search;

import com.google.inject.Inject;
import com.google.inject.Provider;
import common.SunriseInitializationException;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.SortExpression;
import play.Configuration;
import play.Logger;
import productcatalog.productoverview.search.SortConfig;
import productcatalog.productoverview.search.SortOption;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

class SortConfigProvider implements Provider<SortConfig> {

    private static final String CONFIG_KEY = "pop.sortProducts.key";
    private static final String CONFIG_OPTIONS = "pop.sortProducts.options";

    private static final String OPTION_LABEL_ATTR = "label";
    private static final String OPTION_VALUE_ATTR = "value";
    private static final String OPTION_EXPR_ATTR = "expr";
    private static final String OPTION_DEFAULT_ATTR = "default";

    private final Configuration configuration;

    @Inject
    public SortConfigProvider(final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SortConfig get() {
        final String key = configuration.getString(CONFIG_KEY, "sort");
        final List<SortOption> options = getOptions(configuration);
        Logger.debug("Provide SortConfig: {}", options.stream().map(SortOption::getValue).collect(toList()));
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
                .orElseThrow(() -> new SunriseInitializationException("Missing sort value: " + optionConfig));
    }

    private static List<SortExpression<ProductProjection>> getExpressions(final Configuration optionConfig) {
        final List<SortExpression<ProductProjection>> expressions = optionConfig.getStringList(OPTION_EXPR_ATTR, emptyList()).stream()
                .map(SortExpression::<ProductProjection>of)
                .collect(toList());
        if (expressions.isEmpty()) {
            throw new SunriseInitializationException("Missing sort expression: " + optionConfig);
        }
        return expressions;
    }
}
