package productcatalog.productoverview.search;

import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.SortExpression;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class SortSelector {

    private final List<String> selectedValues;
    private final SortConfig sortConfig;

    private SortSelector(final List<String> selectedValues, final SortConfig sortConfig) {
        this.selectedValues = selectedValues;
        this.sortConfig = sortConfig;
    }

    public SortConfig getSortConfig() {
        return sortConfig;
    }

    public List<String> getSelectedValues() {
        return selectedValues;
    }

    public List<SortExpression<ProductProjection>> getSelectedSortExpressions() {
        if (!selectedValues.isEmpty()) {
            return selectedValues.stream()
                    .flatMap(value -> sortConfig.getOptions().stream()
                            .filter(option -> option.getValue().equals(value))
                            .findFirst()
                            .map(SortOption::getExpressions).orElse(emptyList()).stream())
                    .collect(toList());
        } else {
            return emptyList();
        }
    }

    public static SortSelector of(final SortConfig sortConfig, final Map<String, List<String>> queryString) {
        final List<String> selectedValues = getSortSelectedValues(sortConfig, queryString);
        return new SortSelector(selectedValues, sortConfig);
    }

    private static List<String> getSortSelectedValues(final SortConfig sortConfig, final Map<String, List<String>> queryString) {
        final List<String> selectedValues = queryString.getOrDefault(sortConfig.getKey(), emptyList()).stream()
                .filter(selectedValue -> isEnabledSortValue(selectedValue, sortConfig))
                .collect(toList());
        return selectedValues.isEmpty() ? sortConfig.getDefaultValue() : selectedValues;
    }

    private static boolean isEnabledSortValue(final String sortValue, final SortConfig sortConfig) {
        return Optional.ofNullable(sortValue)
                .map(value -> sortConfig.getOptions().stream()
                        .anyMatch(option -> value.equals(option.getValue())))
        .orElse(false);
    }
}
