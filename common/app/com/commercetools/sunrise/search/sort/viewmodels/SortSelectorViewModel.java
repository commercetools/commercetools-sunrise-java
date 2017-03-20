package com.commercetools.sunrise.search.sort.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.forms.FormSelectableOptionViewModel;
import com.commercetools.sunrise.framework.viewmodels.ViewModel;

import java.util.List;

public class SortSelectorViewModel extends ViewModel {

    private String key;
    private List<FormSelectableOptionViewModel> list;

    public SortSelectorViewModel() {
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
