package productcatalog.productoverview.search;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.SortExpression;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class SortSelector extends Base {

    private final String key;
    private final List<SortOption> options;
    private final List<SortOption> selectedOptions;
    private final List<SortOption> defaultOptions;

    private SortSelector(final String key, final List<SortOption> options, final List<SortOption> defaultOptions,
                         final List<SortOption> selectedOptions) {
        this.key = key;
        this.options = options;
        this.selectedOptions = selectedOptions;
        this.defaultOptions = defaultOptions;
    }

    public String getKey() {
        return key;
    }

    public List<SortOption> getOptions() {
        return options;
    }

    public List<SortOption> getSelectedOptions() {
        return selectedOptions.isEmpty() ? defaultOptions : selectedOptions;
    }

    public List<SortExpression<ProductProjection>> getSelectedSortExpressions() {
        return getSelectedOptions().stream()
                .flatMap(option -> option.getExpressions().stream())
                .collect(toList());
    }

    public static SortSelector of(final String key, final List<SortOption> options, final List<String> selectedValues) {
        final List<SortOption> selectedOptions = findSelectedOptions(options, selectedValues);
        final List<SortOption> defaultOptions = options.stream().filter(SortOption::isDefault).collect(toList());
        return new SortSelector(key, options, defaultOptions, selectedOptions);
    }

    private static List<SortOption> findSelectedOptions(final List<SortOption> options, final List<String> selectedValues) {
        return selectedValues.stream()
                .map(value -> findOptionByValue(value, options).orElse(null))
                .filter(value -> value != null)
                .collect(toList());
    }

    private static Optional<SortOption> findOptionByValue(final String value, final List<SortOption> options) {
        return options.stream()
                .filter(option -> option.getValue().equals(value))
                .findFirst();
    }
}
