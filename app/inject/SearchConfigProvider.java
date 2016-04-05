package inject;

import com.google.inject.Inject;
import com.google.inject.Provider;
import play.Configuration;
import productcatalog.productoverview.search.*;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

class SearchConfigProvider implements Provider<SearchConfig> {
    private static final int MAX_PAGE_SIZE = 500;

    private static final String CONFIG_DISPLAY_KEY = "pop.productsPerPage.key";
    private static final String CONFIG_DISPLAY_OPTIONS = "pop.productsPerPage.options";
    private static final String CONFIG_DISPLAY_DEFAULT = "pop.productsPerPage.default";
    private static final String CONFIG_DISPLAY_ENABLE_ALL = "pop.productsPerPage.enableAll";

    private static final String CONFIG_SORT_KEY = "pop.sortProducts.key";
    private static final String CONFIG_SORT_OPTIONS = "pop.sortProducts.options";
    private static final String CONFIG_SORT_DEFAULT = "pop.sortProducts.default";
    private static final String SORT_OPTION_LABEL_ATTR = "label";
    private static final String SORT_OPTION_VALUE_ATTR = "value";
    private static final String SORT_OPTION_EXPR_ATTR = "expr";

    private static final String CONFIG_FACETS = "pop.facets";
    private static final String FACETS_KEY_ATTR = "key";
    private static final String FACETS_LABEL_ATTR = "label";
    private static final String FACETS_EXPR_ATTR = "expr";
    private static final String FACETS_TYPE_ATTR = "type";
    private static final String FACETS_COUNT_ATTR = "count";

    private static final String FACETS_LIMIT_ATTR = "limit";
    private static final String FACETS_MATCHING_ALL_ATTR = "matchingAll";
    private static final String FACETS_MULTI_SELECT_ATTR = "multiSelect";
    private static final String FACETS_THRESHOLD_ATTR = "threshold";

    private static final String FACETS_MAPPER_ATTR = "mapper";
    private static final String FACETS_MAPPER_TYPE_ATTR = "type";
    private static final String FACETS_MAPPER_VALUES_ATTR = "values";

    private final Configuration configuration;

    @Inject
    public SearchConfigProvider(final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SearchConfig get() {
        final String paginationKey = configuration.getString("pop.pagination.key", "page");
        final String searchProductsKey = configuration.getString("pop.searchTerm.key", "q");

        final DisplayConfig displayConfig = getDisplayConfig(configuration);
        final SortConfig sortConfig = getSortConfig(configuration);
        final FacetsConfig facetsConfig = getFacetsConfig(configuration);
        //Logger.debug("Provide SearchConfig: sort {}", sortConfig);
        return SearchConfig.of(paginationKey, searchProductsKey, displayConfig, sortConfig, facetsConfig);
    }

    private static DisplayConfig getDisplayConfig(final Configuration configuration) {
        final String key = configuration.getString(CONFIG_DISPLAY_KEY, "display");
        final int defaultValue = configuration.getInt(CONFIG_DISPLAY_DEFAULT, 24);
        final List<Integer> options = configuration.getIntList(CONFIG_DISPLAY_OPTIONS, asList(24, 48));
        if (!options.stream().allMatch(SearchConfigProvider::isValidDisplayValue)) {
            throw new SunriseInitializationException(String.format("Products per page options are not within bounds [0, %d]: %s",
                    MAX_PAGE_SIZE, options));
        }
        final boolean enableAll = configuration.getBoolean(CONFIG_DISPLAY_ENABLE_ALL, false);
        return DisplayConfig.of(key, options, defaultValue, enableAll);
    }

    private static SortConfig getSortConfig(final Configuration configuration) {
        final String key = configuration.getString(CONFIG_SORT_KEY, "display");
        final List<String> defaultValue = configuration.getStringList(CONFIG_SORT_DEFAULT, emptyList());
        final List<SortOption> options = configuration.getConfigList(CONFIG_SORT_OPTIONS, emptyList()).stream()
                .map(SearchConfigProvider::initializeSortOption)
                .collect(toList());
        return SortConfig.of(key, options, defaultValue);
    }

    private static SortOption initializeSortOption(final Configuration optionConfig) {
        final String label = optionConfig.getString(SORT_OPTION_LABEL_ATTR, "");
        final String value = optionConfig.getString(SORT_OPTION_VALUE_ATTR, "");
        final List<String> expressions = optionConfig.getStringList(SORT_OPTION_EXPR_ATTR, emptyList());
        if (!expressions.isEmpty()) {
            return SortOption.of(value, label, expressions);
        } else {
            throw new SunriseInitializationException("Missing sort expression: " + optionConfig);
        }
    }

    private static FacetsConfig getFacetsConfig(final Configuration configuration) {
        final List<FacetConfig> facetConfigs = configuration.getConfigList(CONFIG_FACETS, emptyList()).stream()
                .map(SearchConfigProvider::getFacetConfig)
                .collect(toList());
        return FacetsConfig.of(facetConfigs);
    }

    private static FacetConfig getFacetConfig(final Configuration facetConfig) {
        final SunriseFacetType type = getFacetType(facetConfig);
        final String key = facetConfig.getString(FACETS_KEY_ATTR, "");
        final String label = facetConfig.getString(FACETS_LABEL_ATTR, "");
        final String expr = Optional.ofNullable(facetConfig.getString(FACETS_EXPR_ATTR))
                .orElseThrow(() -> new SunriseInitializationException("Missing facet expression: " + facetConfig));
        final boolean count = facetConfig.getBoolean(FACETS_COUNT_ATTR, true);
        final boolean matchingAll = facetConfig.getBoolean(FACETS_MATCHING_ALL_ATTR, false);
        final boolean multiSelect = facetConfig.getBoolean(FACETS_MULTI_SELECT_ATTR, true);
        final Long limit = facetConfig.getLong(FACETS_LIMIT_ATTR);
        final Long threshold = facetConfig.getLong(FACETS_THRESHOLD_ATTR, 1L);
        final FacetMapperConfig mapperConfig = getFacetMapperConfig(facetConfig).orElse(null);
        return FacetConfig.of(type, key, label, expr, count, matchingAll, multiSelect, limit, threshold, mapperConfig);
    }

    private static SunriseFacetType getFacetType(final Configuration facetConfig) {
        final String type = facetConfig.getString(FACETS_TYPE_ATTR, "");
        try {
            return SunriseFacetType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new SunriseInitializationException("Not recognized facet type: " + type);
        }
    }

    private static Optional<FacetMapperConfig> getFacetMapperConfig(final Configuration facetConfig) {
        return Optional.ofNullable(facetConfig.getConfig(FACETS_MAPPER_ATTR))
                .map(config -> {
                    final String type = config.getString(FACETS_MAPPER_TYPE_ATTR, "");
                    final List<String> values = config.getStringList(FACETS_MAPPER_VALUES_ATTR, emptyList());
                    return FacetMapperConfig.of(type, values);
                });
    }

    private static boolean isValidDisplayValue(final int value) {
        return value >= 0 && value <= MAX_PAGE_SIZE;
    }
}
