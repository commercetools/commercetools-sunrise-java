package productcatalog.productoverview.search;

import common.contexts.UserContext;
import common.i18n.I18nIdentifier;
import common.i18n.I18nResolver;
import common.models.SelectableData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class DisplayCriteria {
    private static final int ALL_PRODUCTS_PAGE_SIZE = 500;

    private final int selectedValue;
    private final DisplayConfig displayConfig;
    private final UserContext userContext;
    private final I18nResolver i18nResolver;

    private DisplayCriteria(final int selectedValue, final DisplayConfig displayConfig,
                            final UserContext userContext, final I18nResolver i18nResolver) {
        this.selectedValue = selectedValue;
        this.displayConfig = displayConfig;
        this.userContext = userContext;
        this.i18nResolver = i18nResolver;
    }

    public DisplaySelectorBean boundDisplaySelector() {
        final List<SelectableData> displaySelectableData = displayConfig.getOptions().stream()
                .map(this::optionToSelectableData)
                .collect(toList());
        optionAllToSelectableData().ifPresent(displaySelectableData::add);
        return new DisplaySelectorBean(displayConfig.getKey(), displaySelectableData);
    }

    public int getSelectedPageSize() {
        return selectedValue;
    }

    private Optional<SelectableData> optionAllToSelectableData() {
        if (displayConfig.isEnableAll()) {
            final String label = i18nResolver.getOrEmpty(userContext.locales(), I18nIdentifier.of("catalog:displaySelector.all"));
            final SelectableData selectableData = optionToSelectableData(label, ALL_PRODUCTS_PAGE_SIZE);
            return Optional.of(selectableData);
        } else {
            return Optional.empty();
        }
    }

    private SelectableData optionToSelectableData(final int option) {
        return optionToSelectableData(String.valueOf(option), option);
    }

    private SelectableData optionToSelectableData(final String label, final int value) {
        final SelectableData displayOption = new SelectableData(label, String.valueOf(value));
        if (selectedValue == value) {
            displayOption.setSelected(true);
        }
        return displayOption;
    }

    public static DisplayCriteria of(final DisplayConfig displayConfig, final Map<String, List<String>> queryString,
                                     final UserContext userContext, final I18nResolver i18nResolver) {
        final int selectedValue = getDisplaySelectedValue(displayConfig, queryString);
        return new DisplayCriteria(selectedValue, displayConfig, userContext, i18nResolver);
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