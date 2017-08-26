package com.commercetools.sunrise.framework.reverserouters.myaccount.recoverpassword;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

/**
 * The reverse router for the reset password controller.
 */
@ImplementedBy(SimpleRecoverPasswordReverseRouterByReflection.class)
public interface SimpleRecoverPasswordReverseRouter {
    String REQUEST_RECOVERY_EMAIL_PAGE = "requestRecoveryEmailPageCall";

    Call requestRecoveryEmailPageCall(final String languageTag);

    String REQUEST_RECOVERY_EMAIL_PROCESS = "requestRecoveryEmailProcessCall";

    Call requestRecoveryEmailProcessCall(final String languageTag);

    String RESET_PASSWORD_PAGE = "resetPasswordPageCall";

    Call resetPasswordPageCall(final String languageTag, final String resetToken);

    String RESET_PASSWORD_PROCESS = "resetPasswordProcessCall";

    Call resetPasswordProcessCall(final String languageTag, final String resetToken);
}
