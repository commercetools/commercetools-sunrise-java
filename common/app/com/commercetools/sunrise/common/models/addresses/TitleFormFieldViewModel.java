package com.commercetools.sunrise.common.models.addresses;

import com.commercetools.sunrise.common.models.ViewModel;

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
