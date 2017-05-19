package com.commercetools.sunrise.framework.reverserouters.myaccount.changepassword;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultChangePasswordReverseRouter.class)
public interface ChangePasswordReverseRouter extends SimpleChangePasswordReverseRouter, LocalizedReverseRouter {

    default Call changePasswordProcessCall() {
        return changePasswordProcessCall(locale().toLanguageTag());
    }
}
