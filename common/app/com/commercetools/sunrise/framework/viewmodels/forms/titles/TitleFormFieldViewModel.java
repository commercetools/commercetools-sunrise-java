package com.commercetools.sunrise.framework.viewmodels.forms.titles;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

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
