package com.commercetools.sunrise.framework.reverserouters.common;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionLocalizationLocalizedReverseRouter.class)
public interface LocalizationReverseRouter extends LocalizationSimpleReverseRouter, LocalizedReverseRouter {

    default Call changeCountryProcessCall() {
        return changeCountryProcessCall(languageTag());
    }
}
