package com.commercetools.sunrise.common.models.addresses;

import com.commercetools.sunrise.common.models.ViewModel;

import java.util.List;

public class CountryFormFieldViewModel extends ViewModel {

    private List<CountryFormSelectableOptionViewModel> list;

    public List<CountryFormSelectableOptionViewModel> getList() {
        return list;
    }

    public void setList(final List<CountryFormSelectableOptionViewModel> list) {
        this.list = list;
    }
}
