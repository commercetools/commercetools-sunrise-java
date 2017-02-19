package com.commercetools.sunrise.common.search.sort;

import com.commercetools.sunrise.framework.SunriseConfigurationException;
import com.commercetools.sunrise.common.forms.FormSettingsWithOptions;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.SortExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Singleton
public final class SortFormSettings extends FormSettingsWithOptions<SortFormOption> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SortFormSettings.class);

    private static final String CONFIG_KEY = "pop.sortProducts.key";
    private static final String CONFIG_OPTIONS = "pop.sortProducts.options";

    private static final String OPTION_LABEL_ATTR = "label";
    private static final String OPTION_VALUE_ATTR = "value";
    private static final String OPTION_EXPR_ATTR = "expr";
    private static final String OPTION_DEFAULT_ATTR = "default";

    private static final String DEFAULT_KEY = "sort";

    @Inject
    SortFormSettings(final Configuration configuration) {
        super(key(configuration), options(configuration));
        LOGGER.debug("Provide SortConfig: {}", getOptions().stream().map(SortFormOption::getValue).collect(toList()));
    }

    @Override
    public List<SortFormOption> getOptions() {
        return super.getOptions();
    }

    @Override
    public String getFieldName() {
        return super.getFieldName();
    }

    @Override
    public Optional<SortFormOption> findDefaultOption() {
        return super.findDefaultOption();
    }

    private static String key(final Configuration configuration) {
        return configuration.getString(CONFIG_KEY, DEFAULT_KEY);
    }

    private static List<SortFormOption> options(final Configuration configuration) {
        return configuration.getConfigList(CONFIG_OPTIONS, emptyList()).stream()
                .map(SortFormSettings::initializeFormOption)
                .collect(toList());
    }

    private static SortFormOption initializeFormOption(final Configuration optionConfig) {
        return SortFormOption.of(
                extractLabel(optionConfig),
                extractValue(optionConfig),
                extractExpressions(optionConfig),
                extractIsDefault(optionConfig));
    }

    private static String extractLabel(final Configuration optionConfig) {
        return optionConfig.getString(OPTION_LABEL_ATTR, "");
    }

    private static String extractValue(final Configuration optionConfig) {
        return Optional.ofNullable(optionConfig.getString(OPTION_VALUE_ATTR))
                .orElseThrow(() -> new SunriseConfigurationException("Missing sort value", OPTION_VALUE_ATTR, CONFIG_OPTIONS));
    }

    private static List<SortExpression<ProductProjection>> extractExpressions(final Configuration optionConfig) {
        return optionConfig.getStringList(OPTION_EXPR_ATTR, emptyList()).stream()
                .filter(expr -> !expr.isEmpty())
                .map(SortExpression::<ProductProjection>of)
                .collect(toList());
    }

    private static Boolean extractIsDefault(final Configuration optionConfig) {
        return optionConfig.getBoolean(OPTION_DEFAULT_ATTR, false);
    }
}
