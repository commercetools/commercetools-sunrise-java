package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.models.ViewModel;

import java.util.List;

public class CountryFormFieldBean extends ViewModel {

    private List<CountryFormSelectableOptionBean> list;

    public List<CountryFormSelectableOptionBean> getList() {
        return list;
    }

    public void setList(final List<CountryFormSelectableOptionBean> list) {
        this.list = list;
    }
}
