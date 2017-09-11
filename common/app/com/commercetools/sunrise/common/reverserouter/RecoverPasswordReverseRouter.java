package com.commercetools.sunrise.common.reverserouter;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

/**
 * The reverse router for the reset password controller.
 */
@ImplementedBy(ReflectionRecoverPasswordReverseRouter.class)
public interface RecoverPasswordReverseRouter {

    Call showRequestRecoveryEmailForm(final String languageTag);

    Call processRequestRecoverEmailForm(final String languageTag);

    Call showResetPasswordForm(final String languageTag, final String resetToken);

    Call processResetPasswordForm(final String languageTag, final String resetToken);
}
