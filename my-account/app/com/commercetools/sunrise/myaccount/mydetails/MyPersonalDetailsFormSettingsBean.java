package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.common.forms.TitleFormFieldBean;
import com.commercetools.sunrise.common.models.ViewModel;

public class MyPersonalDetailsFormSettingsBean extends ViewModel {

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
