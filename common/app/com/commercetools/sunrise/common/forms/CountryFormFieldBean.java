package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.models.FormSelectableOptionBean;

import java.util.List;

public class CountryFormFieldBean {

    private List<FormSelectableOptionBean> list;

    public List<FormSelectableOptionBean> getList() {
        return list;
    }

    public void setList(final List<FormSelectableOptionBean> list) {
        this.list = list;
    }
}
