package com.commercetools.sunrise.framework.reverserouters.myaccount.changepassword;

import com.commercetools.sunrise.framework.reverserouters.ReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultChangePasswordReverseRouter.class)
public interface ChangePasswordReverseRouter extends ReverseRouter {

    String CHANGE_PASSWORD_PAGE = "changePasswordPageCall";

    Call changePasswordPageCall();

    String CHANGE_PASSWORD_PROCESS = "changePasswordProcessCall";

    Call changePasswordProcessCall();
}
