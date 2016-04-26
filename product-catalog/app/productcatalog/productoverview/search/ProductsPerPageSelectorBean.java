package productcatalog.productoverview.search;

import common.contexts.UserContext;
import common.template.i18n.I18nResolver;
import common.models.SelectableBean;
import io.sphere.sdk.models.Base;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProductsPerPageSelectorBean extends Base {
    private String key;
    private List<SelectableBean> list;

    public ProductsPerPageSelectorBean() {
    }

    public ProductsPerPageSelectorBean(final ProductsPerPageSelector productsPerPageSelector, final UserContext userContext, final I18nResolver i18nResolver) {
        this.key = productsPerPageSelector.getKey();
        this.list = productsPerPageSelector.getOptions().stream()
                .map(option -> optionToSelectableData(option, productsPerPageSelector))
                .collect(toList());
    }

    public ProductsPerPageSelectorBean(final String key, final List<SelectableBean> list) {
        this.key = key;
        this.list = list;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public List<SelectableBean> getList() {
        return list;
    }

    public void setList(final List<SelectableBean> list) {
        this.list = list;
    }

    private static SelectableBean optionToSelectableData(final ProductsPerPageOption option, final ProductsPerPageSelector productsPerPageSelector) {
        final SelectableBean displayOption = new SelectableBean(option.getLabel(), option.getValue());
        if (productsPerPageSelector.getSelectedPageSize() == option.getAmount()) {
            displayOption.setSelected(true);
        }
        return displayOption;
    }
}
