package productcatalog.productoverview.search;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

public class DisplayCriteria {

    private static final int ALL_PRODUCTS_PAGE_SIZE = 500;
    private final int selectedValue;
    private final DisplayConfig displayConfig;

    private DisplayCriteria(final int selectedValue, final DisplayConfig displayConfig) {
        this.selectedValue = selectedValue;
        this.displayConfig = displayConfig;
    }

    public DisplayConfig getDisplayConfig() {
        return displayConfig;
    }

    public int getSelectedPageSize() {
        return selectedValue;
    }

    public static int getAllProductsPageSize() {
        return ALL_PRODUCTS_PAGE_SIZE;
    }

    public static DisplayCriteria of(final DisplayConfig displayConfig, final Map<String, List<String>> queryString) {
        final int selectedValue = getDisplaySelectedValue(displayConfig, queryString);
        return new DisplayCriteria(selectedValue, displayConfig);
    }

    private static int getDisplaySelectedValue(final DisplayConfig displayConfig, final Map<String, List<String>> queryString) {
        return queryString.getOrDefault(displayConfig.getKey(), emptyList()).stream()
                .map(DisplayCriteria::toDisplayValue)
                .filter(selectedValue -> isEnabledDisplayValue(displayConfig, selectedValue))
                .findFirst()
                .orElse(displayConfig.getDefaultValue());
    }

    @Nullable
    private static Integer toDisplayValue(final String valueAsString) {
        try {
            return Integer.valueOf(valueAsString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static boolean isEnabledDisplayValue(final DisplayConfig displayConfig, @Nullable final Integer displayValue) {
        if (displayValue != null) {
            final boolean valueIsValid = displayConfig.getOptions().contains(displayValue);
            final boolean isAllProducts = displayConfig.isEnableAll() && displayValue == ALL_PRODUCTS_PAGE_SIZE;
            return valueIsValid || isAllProducts;
        } else {
            return false;
        }
    }
}