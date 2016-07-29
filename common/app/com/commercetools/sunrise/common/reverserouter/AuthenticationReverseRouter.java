package com.commercetools.sunrise.common.reverserouter;

import play.mvc.Call;

public interface AuthenticationReverseRouter {
    Call showLogInForm(final String languageTag);

    Call processLogInForm(final String languageTag);

    Call processSignUpForm(final String languageTag);

    Call processLogOut(final String languageTag);
}
