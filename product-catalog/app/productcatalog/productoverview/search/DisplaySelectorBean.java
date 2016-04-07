package productcatalog.productoverview.search;

import common.contexts.UserContext;
import common.i18n.I18nIdentifier;
import common.i18n.I18nResolver;
import common.models.SelectableData;
import io.sphere.sdk.models.Base;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class DisplaySelectorBean extends Base {
    private String key;
    private List<SelectableData> list;

    public DisplaySelectorBean() {
    }

    public DisplaySelectorBean(final DisplayCriteria displayCriteria, final UserContext userContext, final I18nResolver i18nResolver) {
        final DisplayConfig displayConfig = displayCriteria.getDisplayConfig();
        this.key = displayConfig.getKey();
        this.list = displayConfig.getOptions().stream()
                .map(option -> optionToSelectableData(option, displayCriteria))
                .collect(toList());
        optionAllToSelectableData(displayCriteria, userContext, i18nResolver).ifPresent(this.list::add);
    }

    public DisplaySelectorBean(final String key, final List<SelectableData> list) {
        this.key = key;
        this.list = list;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public List<SelectableData> getList() {
        return list;
    }

    public void setList(final List<SelectableData> list) {
        this.list = list;
    }

    private static Optional<SelectableData> optionAllToSelectableData(final DisplayCriteria displayCriteria,
                                                                      final UserContext userContext, final I18nResolver i18nResolver) {
        if (displayCriteria.getDisplayConfig().isEnableAll()) {
            final String label = i18nResolver.getOrEmpty(userContext.locales(), I18nIdentifier.of("catalog:displaySelector.all"));
            final SelectableData selectableData = optionToSelectableData(label, DisplayCriteria.getAllProductsPageSize(), displayCriteria);
            return Optional.of(selectableData);
        } else {
            return Optional.empty();
        }
    }

    private static SelectableData optionToSelectableData(final int option, final DisplayCriteria displayCriteria) {
        return optionToSelectableData(String.valueOf(option), option, displayCriteria);
    }

    private static SelectableData optionToSelectableData(final String label, final int value, final DisplayCriteria displayCriteria) {
        final SelectableData displayOption = new SelectableData(label, String.valueOf(value));
        if (displayCriteria.getSelectedPageSize() == value) {
            displayOption.setSelected(true);
        }
        return displayOption;
    }
}
