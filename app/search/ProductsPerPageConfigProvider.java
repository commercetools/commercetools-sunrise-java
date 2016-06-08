package search;

import common.SunriseInitializationException;
import play.Configuration;
import play.Logger;
import productcatalog.productoverview.search.productsperpage.ProductsPerPageConfig;
import productcatalog.productoverview.search.productsperpage.ProductsPerPageOption;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

class ProductsPerPageConfigProvider implements Provider<ProductsPerPageConfig> {

    private static final int MIN_PAGE_SIZE = 0;
    private static final int MAX_PAGE_SIZE = 500;

    private static final String CONFIG_KEY = "pop.productsPerPage.key";
    private static final String CONFIG_OPTIONS = "pop.productsPerPage.options";
    private static final String CONFIG_DEFAULT = "pop.productsPerPage.default";

    private static final String OPTION_LABEL_ATTR = "label";
    private static final String OPTION_VALUE_ATTR = "value";
    private static final String OPTION_AMOUNT_ATTR = "amount";

    @Inject
    private Configuration configuration;

    @Override
    public ProductsPerPageConfig get() {
        final String key = configuration.getString(CONFIG_KEY, "ppp");
        final List<ProductsPerPageOption> options = getOptions(configuration);
        final int defaultAmount = getDefaultAmount();
        Logger.debug("Provide ProductsPerPageConfig: {}", options.stream().map(ProductsPerPageOption::getValue).collect(toList()));
        return ProductsPerPageConfig.of(key, options, defaultAmount);
    }

    private static List<ProductsPerPageOption> getOptions(final Configuration configuration) {
        return configuration.getConfigList(CONFIG_OPTIONS, emptyList()).stream()
                .map(ProductsPerPageConfigProvider::initializeOption)
                .collect(toList());
    }

    private static ProductsPerPageOption initializeOption(final Configuration optionConfig) {
        final String label = optionConfig.getString(OPTION_LABEL_ATTR, "");
        final String value = Optional.ofNullable(optionConfig.getString(OPTION_VALUE_ATTR))
                .orElseThrow(() -> new SunriseInitializationException("Missing value for products per page: " + optionConfig));
        final int amount = Optional.ofNullable(optionConfig.getInt(OPTION_AMOUNT_ATTR))
                .orElseThrow(() -> new SunriseInitializationException("Missing amount for products per page: " + optionConfig));
        if (isValidAmount(amount)) {
            return ProductsPerPageOption.of(value, label, amount);
        } else {
            throw new SunriseInitializationException(String.format("Products per page options are not within bounds [%d, %d]: %s",
                    MIN_PAGE_SIZE, MAX_PAGE_SIZE, optionConfig));
        }
    }

    private Integer getDefaultAmount() {
        return Optional.ofNullable(configuration.getInt(CONFIG_DEFAULT))
                .orElseThrow(() -> new SunriseInitializationException("Missing default amount for products per page"));
    }

    private static boolean isValidAmount(final int amount) {
        return amount >= MIN_PAGE_SIZE && amount <= MAX_PAGE_SIZE;
    }
}
