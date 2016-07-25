package com.commercetools.sunrise.common.models;

import io.sphere.sdk.models.Base;

import java.util.List;

public class TitleFormFieldBean extends Base {

    private static final String CONFIG_TITLE_OPTIONS = "form.titles";

    private List<FormSelectableOptionBean> list;

    public TitleFormFieldBean() {
    }

    public List<FormSelectableOptionBean> getList() {
        return list;
    }

    public void setList(final List<FormSelectableOptionBean> list) {
        this.list = list;
    }
}
