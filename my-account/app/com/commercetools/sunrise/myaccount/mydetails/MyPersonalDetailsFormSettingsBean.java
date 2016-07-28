package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.common.forms.TitleFormFieldBean;
import com.commercetools.sunrise.common.models.ModelBean;

public class MyPersonalDetailsFormSettingsBean extends ModelBean {

    private TitleFormFieldBean title;

    public MyPersonalDetailsFormSettingsBean() {
    }

    public TitleFormFieldBean getTitle() {
        return title;
    }

    public void setTitle(final TitleFormFieldBean title) {
        this.title = title;
    }
}
