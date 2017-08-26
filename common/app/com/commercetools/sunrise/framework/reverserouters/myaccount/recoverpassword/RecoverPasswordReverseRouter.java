package com.commercetools.sunrise.framework.reverserouters.myaccount.recoverpassword;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultRecoverPasswordReverseRouter.class)
public interface RecoverPasswordReverseRouter extends SimpleRecoverPasswordReverseRouter, LocalizedReverseRouter {

    default Call resetPasswordProcessCall(final String resetToken) {
        return resetPasswordProcessCall(locale().toLanguageTag(), resetToken);
    }

    default Call resetPasswordPageCall(final String resetToken) {
        return resetPasswordPageCall(locale().toLanguageTag(), resetToken);
    }

    default Call requestRecoveryEmailPageCall() {
        return requestRecoveryEmailPageCall(locale().toLanguageTag());
    }

    default Call requestRecoveryEmailProcessCall() {
        return requestRecoveryEmailProcessCall(locale().toLanguageTag());
    }

}
