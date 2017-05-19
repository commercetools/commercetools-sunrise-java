package com.commercetools.sunrise.framework.reverserouters.myaccount.changepassword;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(SimpleChangePasswordReverseRouterByReflection.class)
public interface SimpleChangePasswordReverseRouter {

    String CHANGE_PASSWORD_PAGE = "changePasswordPageCall";

    String CHANGE_PASSWORD_PROCESS = "changePasswordProcessCall";

    Call changePasswordProcessCall(final String languageTag);
}
