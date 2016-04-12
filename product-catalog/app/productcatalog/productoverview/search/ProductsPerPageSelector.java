package productcatalog.productoverview.search;

import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ProductsPerPageSelector extends Base {

    private final String key;
    private final List<ProductsPerPageOption> options;
    private final int defaultAmount;
    private final Optional<ProductsPerPageOption> selectedOption;

    public ProductsPerPageSelector(final String key, final List<ProductsPerPageOption> options, final int defaultAmount,
                                   @Nullable final ProductsPerPageOption selectedOption) {
        this.key = key;
        this.options = options;
        this.defaultAmount = defaultAmount;
        this.selectedOption = Optional.ofNullable(selectedOption);
    }

    public String getKey() {
        return key;
    }

    public List<ProductsPerPageOption> getOptions() {
        return options;
    }

    public int getSelectedPageSize() {
        return selectedOption.map(ProductsPerPageOption::getAmount).orElse(defaultAmount);
    }

    public static ProductsPerPageSelector of(final String key, final List<ProductsPerPageOption> options,
                                             final int defaultAmount, final List<String> selectedValues) {
        final ProductsPerPageOption selectedOption = findFirstSelectedOption(selectedValues, options).orElse(null);
        return new ProductsPerPageSelector(key, options, defaultAmount, selectedOption);
    }

    private static Optional<ProductsPerPageOption> findFirstSelectedOption(final List<String> selectedValues,
                                                                           final List<ProductsPerPageOption> options) {
        return options.stream()
                .filter(option -> selectedValues.contains(option.getValue()))
                .findFirst();
    }
}