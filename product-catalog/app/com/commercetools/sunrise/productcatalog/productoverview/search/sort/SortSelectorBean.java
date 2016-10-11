package com.commercetools.sunrise.productcatalog.productoverview.search.sort;

import com.commercetools.sunrise.common.models.FormSelectableOptionBean;
import com.commercetools.sunrise.common.models.ViewModel;

import java.util.List;

public class SortSelectorBean extends ViewModel {

    private String key;
    private List<FormSelectableOptionBean> list;

    public SortSelectorBean() {
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
}
