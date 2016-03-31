package inject;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.SortExpression;
import play.Configuration;
import productcatalog.productoverview.search.DisplayConfig;
import productcatalog.productoverview.search.SearchConfig;
import productcatalog.productoverview.search.SortConfig;
import productcatalog.productoverview.search.SortOption;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

class SearchConfigProvider implements Provider<SearchConfig> {
    private static final int MAX_PAGE_SIZE = 500;

    private static final String CONFIG_DISPLAY_KEY = "pop.productsPerPage.key";
    private static final String CONFIG_DISPLAY_OPTIONS = "pop.productsPerPage.options";
    private static final String CONFIG_DISPLAY_DEFAULT = "pop.productsPerPage.default";

    private static final String CONFIG_SORT_KEY = "pop.sortProducts.key";
    private static final String CONFIG_SORT_OPTIONS = "pop.sortProducts.options";
    private static final String CONFIG_SORT_DEFAULT = "pop.sortProducts.default";
    private static final String SORT_OPTION_LABEL_ATTR = "label";
    private static final String SORT_OPTION_VALUE_ATTR = "value";
    private static final String SORT_OPTION_EXPR_ATTR = "expr";

    private final Configuration configuration;

    @Inject
    public SearchConfigProvider(final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SearchConfig get() {
        final List<String> sortedSizes = configuration.getStringList("pop.facet.size", emptyList());

        final String paginationKey = configuration.getString("pop.pagination.key", "page");
        final String searchProductsKey = configuration.getString("pop.searchTerm.key", "q");

        final DisplayConfig displayConfig = getDisplayConfig(configuration);
        final SortConfig sortConfig = getSortConfig(configuration);
        //Logger.debug("Provide SearchConfig: sort {}", sortConfig);
        return new SearchConfig(paginationKey, searchProductsKey, sortConfig, displayConfig, sortedSizes);
    }

    private static DisplayConfig getDisplayConfig(final Configuration configuration) {
        final String key = configuration.getString(CONFIG_DISPLAY_KEY, "display");
        final int defaultValue = configuration.getInt(CONFIG_DISPLAY_DEFAULT, 24);
        final List<Integer> options = configuration.getIntList(CONFIG_DISPLAY_OPTIONS, asList(24, 48, 0));
        if (!options.stream().allMatch(SearchConfigProvider::isValidDisplayValue)) {
            throw new SunriseInitializationException(String.format("Products per page options are not within bounds [0, %d]: %s",
                    MAX_PAGE_SIZE, options));
        }
        return new DisplayConfig(key, options, defaultValue);
    }

    private static SortConfig getSortConfig(final Configuration configuration) {
        final String key = configuration.getString(CONFIG_SORT_KEY, "display");
        final List<String> defaultValue = configuration.getStringList(CONFIG_SORT_DEFAULT, emptyList());
        final List<SortOption<ProductProjection>> options = configuration.getConfigList(CONFIG_SORT_OPTIONS, emptyList()).stream()
                .map(SearchConfigProvider::initializeSortOption)
                .collect(toList());
        return new SortConfig(key, options, defaultValue);
    }

    private static SortOption<ProductProjection> initializeSortOption(final Configuration optionConfig) {
        final String label = optionConfig.getString(SORT_OPTION_LABEL_ATTR, "");
        final String value = optionConfig.getString(SORT_OPTION_VALUE_ATTR, "");
        final List<SortExpression<ProductProjection>> exprList = optionConfig.getStringList(SORT_OPTION_EXPR_ATTR, emptyList()).stream()
                .map(SearchConfigProvider::getProductSortExpression)
                .collect(toList());
        if (!exprList.isEmpty()) {
            return new SortOption<>(label, value, exprList);
        } else {
            throw new SunriseInitializationException("Missing sort expression: " + optionConfig);
        }
    }

    private static SortExpression<ProductProjection> getProductSortExpression(final String expr) {
        return SortExpression.of(expr);
    }

    private static boolean isValidDisplayValue(final int value) {
        return value >= 0 && value <= MAX_PAGE_SIZE;
    }
}
