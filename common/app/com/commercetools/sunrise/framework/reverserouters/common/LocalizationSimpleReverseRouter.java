package com.commercetools.sunrise.framework.reverserouters.common;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionLocalizationReverseRouter.class)
interface LocalizationSimpleReverseRouter {

    String CHANGE_LANGUAGE_PROCESS = "changeLanguageProcess";

    Call changeLanguageProcessCall();

    String CHANGE_COUNTRY_PROCESS = "changeCountryProcess";

    Call changeCountryProcessCall(final String languageTag);
}
