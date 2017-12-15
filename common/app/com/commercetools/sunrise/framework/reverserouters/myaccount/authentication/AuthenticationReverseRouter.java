package com.commercetools.sunrise.framework.reverserouters.myaccount.authentication;

import com.commercetools.sunrise.framework.reverserouters.ReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultAuthenticationReverseRouter.class)
public interface AuthenticationReverseRouter extends ReverseRouter {

    String LOG_IN_PAGE = "logInPageCall";

    Call logInPageCall();

    String LOG_IN_PROCESS = "logInProcessCall";

    Call logInProcessCall();

    String SIGN_UP_PROCESS = "signUpProcessCall";

    Call signUpProcessCall();

    String LOG_OUT_PROCESS = "logOutProcessCall";

    Call logOutProcessCall();
}
