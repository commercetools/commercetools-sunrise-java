package com.commercetools.sunrise.core.viewmodels.forms.titles;

import com.commercetools.sunrise.core.viewmodels.ViewModel;

import java.util.List;

public class TitleFormFieldViewModel extends ViewModel {

    private List<TitleFormSelectableOptionViewModel> list;

    public List<TitleFormSelectableOptionViewModel> getList() {
        return list;
    }

    public void setList(final List<TitleFormSelectableOptionViewModel> list) {
        this.list = list;
    }
}
