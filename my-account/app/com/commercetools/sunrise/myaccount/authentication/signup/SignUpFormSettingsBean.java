package com.commercetools.sunrise.myaccount.authentication.signup;

import com.commercetools.sunrise.common.forms.FormBean;
import com.commercetools.sunrise.common.forms.TitleFormFieldBean;

public class SignUpFormSettingsBean extends FormBean {

    private TitleFormFieldBean title;

    public TitleFormFieldBean getTitle() {
        return title;
    }

    public void setTitle(final TitleFormFieldBean title) {
        this.title = title;
    }
}
