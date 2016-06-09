package productcatalog.productoverview.search.sort;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.SortExpression;

import java.util.List;

import static java.util.stream.Collectors.toList;

public final class SortSelector extends Base {

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

    public static SortSelector of(final String key, final List<SortOption> options, final List<SortOption> defaultOptions,
                                  final List<SortOption> selectedOptions) {
        return new SortSelector(key, options, defaultOptions, selectedOptions);
    }
}
