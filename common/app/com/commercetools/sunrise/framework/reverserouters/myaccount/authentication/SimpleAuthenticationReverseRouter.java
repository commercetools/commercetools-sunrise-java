package com.commercetools.sunrise.framework.reverserouters.myaccount.authentication;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(SimpleAuthenticationReverseRouterByReflection.class)
public interface SimpleAuthenticationReverseRouter {

    String LOG_IN_PAGE = "logInPageCall";

    Call logInPageCall(final String languageTag);

    String LOG_IN_PROCESS = "logInProcessCall";

    Call logInProcessCall(final String languageTag);

    String SIGN_UP_PROCESS = "signUpProcessCall";

    Call signUpProcessCall(final String languageTag);

    String LOG_OUT_PROCESS = "logOutProcessCall";

    Call logOutProcessCall(final String languageTag);
}
