package productcatalog.productoverview.search;

import common.contexts.UserContext;
import common.template.i18n.I18nIdentifier;
import common.template.i18n.I18nResolver;
import common.models.FormSelectableOptionBean;
import io.sphere.sdk.models.Base;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class SortSelectorBean extends Base {
    private String key;
    private List<FormSelectableOptionBean> list;

    public SortSelectorBean() {
    }

    public SortSelectorBean(final SortSelector sortSelector, final UserContext userContext, final I18nResolver i18nResolver) {
        this.key = sortSelector.getKey();
        this.list = sortSelector.getOptions().stream()
                .map(option -> optionToSelectableData(option, sortSelector, userContext, i18nResolver))
                .collect(toList());
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public List<FormSelectableOptionBean> getList() {
        return list;
    }

    public void setList(final List<FormSelectableOptionBean> list) {
        this.list = list;
    }

    private static FormSelectableOptionBean optionToSelectableData(final SortOption option, final SortSelector sortSelector,
                                                                   final UserContext userContext, final I18nResolver i18nResolver) {
        final String label = i18nResolver.getOrKey(userContext.locales(), I18nIdentifier.of(option.getLabel()));
        final FormSelectableOptionBean sortOption = new FormSelectableOptionBean(label, option.getValue());
        if (sortSelector.getSelectedOptions().contains(option)) {
            sortOption.setSelected(true);
        }
        return sortOption;
    }
}
