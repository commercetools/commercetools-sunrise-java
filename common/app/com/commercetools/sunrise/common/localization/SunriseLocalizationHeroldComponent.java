package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.pages.HeroldComponentBase;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.LocalizationReverseRouter;

import javax.inject.Inject;

final class SunriseLocalizationHeroldComponent extends HeroldComponentBase {
    @Inject
    private LocalizationReverseRouter reverseRouter;

    protected void updateMeta(final PageMeta meta) {
        meta.addHalLink(reverseRouter.processChangeLanguageForm(), "selectLanguage");
        meta.addHalLink(reverseRouter.processChangeCountryForm(languageTag()), "selectCountry");
    }
}