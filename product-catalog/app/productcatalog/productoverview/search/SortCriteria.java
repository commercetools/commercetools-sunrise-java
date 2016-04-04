package productcatalog.productoverview.search;

import common.contexts.UserContext;
import common.i18n.I18nIdentifier;
import common.i18n.I18nResolver;
import common.models.SelectableData;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.SortExpression;
import productcatalog.productoverview.SortSelector;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class SortCriteria {

    private final List<String> selectedValues;
    private final SortConfig sortConfig;
    private final UserContext userContext;
    private final I18nResolver i18nResolver;

    private SortCriteria(final List<String> selectedValues, final SortConfig sortConfig,
                         final UserContext userContext, final I18nResolver i18nResolver) {
        this.selectedValues = selectedValues;
        this.sortConfig = sortConfig;
        this.userContext = userContext;
        this.i18nResolver = i18nResolver;
    }

    public SortSelector boundSortSelector() {
        final List<SelectableData> sortSelectableData = sortConfig.getOptions().stream()
                .map(this::optionToSelectableData)
                .collect(toList());
        return new SortSelector(sortConfig.getKey(), sortSelectableData);
    }

    public List<SortExpression<ProductProjection>> getSelectedSortExpressions() {
        if (!selectedValues.isEmpty()) {
            return sortConfig.getOptions().stream()
                    .filter(option -> selectedValues.contains(option.getValue()))
                    .flatMap(option -> option.getExpressions().stream())
                    .collect(toList());
        } else {
            return emptyList();
        }
    }

    private SelectableData optionToSelectableData(final SortOption option) {
        final String label = i18nResolver.get(userContext.locales(), I18nIdentifier.of(option.getLabel()))
                .orElse(option.getLabel());
        final SelectableData sortOption = new SelectableData(label, option.getValue());
        if (selectedValues.contains(option.getValue())) {
            sortOption.setSelected(true);
        }
        return sortOption;
    }

    public static SortCriteria of(final SortConfig sortConfig, final Map<String, List<String>> queryString,
                                  final UserContext userContext, final I18nResolver i18nResolver) {
        final List<String> selectedValues = getSortSelectedValues(sortConfig, queryString);
        return new SortCriteria(selectedValues, sortConfig, userContext, i18nResolver);
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
