package com.commercetools.sunrise.framework.reverserouters.common;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionLocalizationReverseRouter.class)
interface LocalizationSimpleReverseRouter {

    String CHANGE_LANGUAGE_PROCESS = "changeLanguageProcessCall";

    Call changeLanguageProcessCall();

    String CHANGE_COUNTRY_PROCESS = "changeCountryProcessCall";

    Call changeCountryProcessCall(final String languageTag);
}
