package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionAuthenticationReverseRouter.class)
interface AuthenticationSimpleReverseRouter {

    String LOG_IN_PAGE = "logInPage";

    Call logInPageCall(final String languageTag);

    String LOG_IN_PROCESS = "logInProcess";

    Call logInProcessCall(final String languageTag);

    String SIGN_UP_PROCESS = "signUpProcess";

    Call signUpProcessCall(final String languageTag);

    String LOG_OUT_PROCESS = "logOutProcess";

    Call logOutProcessCall(final String languageTag);
}
