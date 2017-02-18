package com.commercetools.sunrise.common.reverserouter.myaccount;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionAuthenticationReverseRouter.class)
interface AuthenticationSimpleReverseRouter {

    Call showLogInForm(final String languageTag);

    Call processLogInForm(final String languageTag);

    Call processSignUpForm(final String languageTag);

    Call processLogOut(final String languageTag);
}
