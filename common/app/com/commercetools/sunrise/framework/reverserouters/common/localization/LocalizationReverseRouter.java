package com.commercetools.sunrise.framework.reverserouters.common.localization;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultLocalizationReverseRouter.class)
public interface LocalizationReverseRouter extends SimpleLocalizationReverseRouter, LocalizedReverseRouter {

    default Call changeCountryProcessCall() {
        return changeCountryProcessCall(locale().toLanguageTag());
    }
}
