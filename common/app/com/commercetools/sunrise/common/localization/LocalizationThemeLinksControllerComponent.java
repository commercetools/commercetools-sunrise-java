package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.LocalizationLocalizedReverseRouter;

import javax.inject.Inject;

final class LocalizationThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final LocalizationLocalizedReverseRouter reverseRouter;

    @Inject
    LocalizationThemeLinksControllerComponent(final LocalizationLocalizedReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.processChangeLanguageForm(), "selectLanguage");
        meta.addHalLink(reverseRouter.processChangeCountryForm(), "selectCountry");
    }
}