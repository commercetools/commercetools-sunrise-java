package com.commercetools.sunrise.common.search.pagination;

import com.commercetools.sunrise.common.SunriseConfigurationException;
import com.commercetools.sunrise.common.forms.FormSettingsWithOptions;
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
public final class ProductsPerPageFormSettings extends FormSettingsWithOptions<ProductsPerPageFormOption> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsPerPageFormSettings.class);

    private static final int MIN_PAGE_SIZE = 0;
    private static final int MAX_PAGE_SIZE = 500;

    private static final String CONFIG_KEY = "pop.productsPerPage.key";
    private static final String CONFIG_OPTIONS = "pop.productsPerPage.options";

    private static final String OPTION_LABEL_ATTR = "label";
    private static final String OPTION_VALUE_ATTR = "value";
    private static final String OPTION_AMOUNT_ATTR = "amount";
    private static final String OPTION_DEFAULT_ATTR = "default";

    private static final String DEFAULT_KEY = "ppp";

    @Inject
    public ProductsPerPageFormSettings(final Configuration configuration) {
        super(key(configuration), options(configuration));
        LOGGER.debug("Provide ProductsPerPageFormConfiguration: {}", getOptions().stream().map(ProductsPerPageFormOption::getFieldValue).collect(toList()));
    }

    @Override
    public String getFieldName() {
        return super.getFieldName();
    }

    @Override
    public List<ProductsPerPageFormOption> getOptions() {
        return super.getOptions();
    }

    @Override
    public Optional<ProductsPerPageFormOption> findDefaultOption() {
        return super.findDefaultOption();
    }

    private static String key(final Configuration configuration) {
        return configuration.getString(CONFIG_KEY, DEFAULT_KEY);
    }

    private static List<ProductsPerPageFormOption> options(final Configuration configuration) {
        return configuration.getConfigList(CONFIG_OPTIONS, emptyList()).stream()
                .map(ProductsPerPageFormSettings::initializeOption)
                .collect(toList());
    }

    private static ProductsPerPageFormOption initializeOption(final Configuration optionConfig) {
        return ProductsPerPageFormOption.of(
                extractValue(optionConfig),
                extractLabel(optionConfig),
                extractAmount(optionConfig),
                extractIsDefault(optionConfig));
    }

    private static String extractLabel(final Configuration optionConfig) {
        return optionConfig.getString(OPTION_LABEL_ATTR, "");
    }

    private static String extractValue(final Configuration optionConfig) {
        return Optional.ofNullable(optionConfig.getString(OPTION_VALUE_ATTR))
                .orElseThrow(() -> new SunriseConfigurationException("Missing products per page value", OPTION_VALUE_ATTR, CONFIG_OPTIONS));
    }

    private static int extractAmount(final Configuration optionConfig) {
        final int amount = Optional.ofNullable(optionConfig.getInt(OPTION_AMOUNT_ATTR))
                .orElseThrow(() -> new SunriseConfigurationException("Missing products per page amount", OPTION_AMOUNT_ATTR, CONFIG_OPTIONS));
        if (!isValidAmount(amount)) {
            throw new SunriseConfigurationException(String.format("Products per page options are not within bounds [%d, %d]: %s",
                    MIN_PAGE_SIZE, MAX_PAGE_SIZE, optionConfig), OPTION_AMOUNT_ATTR, CONFIG_OPTIONS);
        }
        return amount;
    }

    private static Boolean extractIsDefault(final Configuration optionConfig) {
        return optionConfig.getBoolean(OPTION_DEFAULT_ATTR, false);
    }

    private static boolean isValidAmount(final int amount) {
        return amount >= MIN_PAGE_SIZE && amount <= MAX_PAGE_SIZE;
    }
}
