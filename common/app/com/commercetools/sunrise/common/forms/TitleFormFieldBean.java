package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.models.ViewModel;

import java.util.List;

public class TitleFormFieldBean extends ViewModel {

    private List<TitleFormSelectableOptionBean> list;

    public List<TitleFormSelectableOptionBean> getList() {
        return list;
    }

    public void setList(final List<TitleFormSelectableOptionBean> list) {
        this.list = list;
    }
}
