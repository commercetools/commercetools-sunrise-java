package com.commercetools.sunrise.framework.viewmodels.forms.countries;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

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
