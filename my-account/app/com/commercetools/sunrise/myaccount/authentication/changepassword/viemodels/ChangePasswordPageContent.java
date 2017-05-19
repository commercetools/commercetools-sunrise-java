package com.commercetools.sunrise.myaccount.authentication.changepassword.viemodels;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import play.data.Form;

public class ChangePasswordPageContent extends PageContent {

    private Form<?> changePasswordForm;

    public Form<?> getChangePasswordForm() {
        return changePasswordForm;
    }

    void setChangePasswordForm(final Form<?> changePasswordForm) {
        this.changePasswordForm = changePasswordForm;
    }
}
