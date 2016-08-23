package com.commercetools.sunrise.myaccount.authentication.signup;

import com.commercetools.sunrise.common.forms.TitleFormFieldBean;
import com.commercetools.sunrise.common.models.ViewModel;

public class SignUpFormSettingsBean extends ViewModel {

    private TitleFormFieldBean title;

    public SignUpFormSettingsBean() {
    }

    public TitleFormFieldBean getTitle() {
        return title;
    }

    public void setTitle(final TitleFormFieldBean title) {
        this.title = title;
    }
}
