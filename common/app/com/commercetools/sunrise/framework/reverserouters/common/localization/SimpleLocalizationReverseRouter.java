package com.commercetools.sunrise.framework.reverserouters.common.localization;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(SimpleLocalizationReverseRouterByReflection.class)
public interface SimpleLocalizationReverseRouter {

    String CHANGE_LANGUAGE_PROCESS = "changeLanguageProcessCall";

    Call changeLanguageProcessCall();

    String CHANGE_COUNTRY_PROCESS = "changeCountryProcessCall";

    Call changeCountryProcessCall(final String languageTag);
}
