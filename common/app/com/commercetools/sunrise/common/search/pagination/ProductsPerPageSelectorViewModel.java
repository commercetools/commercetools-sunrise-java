package com.commercetools.sunrise.common.search.pagination;

import com.commercetools.sunrise.common.models.FormSelectableOptionViewModel;
import com.commercetools.sunrise.common.models.ViewModel;

import java.util.List;

public class ProductsPerPageSelectorViewModel extends ViewModel {

    private String key;
    private List<FormSelectableOptionViewModel> list;

    public ProductsPerPageSelectorViewModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public List<FormSelectableOptionViewModel> getList() {
        return list;
    }

    public void setList(final List<FormSelectableOptionViewModel> list) {
        this.list = list;
    }
}
