package com.commercetools.sunrise.framework.reverserouters.myaccount.recoverpassword;

import com.commercetools.sunrise.framework.reverserouters.ReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultRecoverPasswordReverseRouter.class)
public interface RecoverPasswordReverseRouter extends ReverseRouter {

    String REQUEST_RECOVERY_EMAIL_PAGE = "requestRecoveryEmailPageCall";

    Call requestRecoveryEmailPageCall();

    String REQUEST_RECOVERY_EMAIL_PROCESS = "requestRecoveryEmailProcessCall";

    Call requestRecoveryEmailProcessCall();

    String RESET_PASSWORD_PAGE = "resetPasswordPageCall";

    Call resetPasswordPageCall(final String resetToken);

    String RESET_PASSWORD_PROCESS = "resetPasswordProcessCall";

    Call resetPasswordProcessCall(final String resetToken);
}
