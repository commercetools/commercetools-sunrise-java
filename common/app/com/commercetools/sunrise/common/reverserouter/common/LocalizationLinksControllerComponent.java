package com.commercetools.sunrise.common.reverserouter.common;

import com.commercetools.sunrise.common.pages.AbstractLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;

import javax.inject.Inject;

public class LocalizationLinksControllerComponent extends AbstractLinksControllerComponent<LocalizationReverseRouter> {

    private final LocalizationReverseRouter reverseRouter;

    @Inject
    protected LocalizationLinksControllerComponent(final LocalizationReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    public final LocalizationReverseRouter getReverseRouter() {
        return reverseRouter;
    }

    @Override
    protected void addLinksToPage(final PageMeta meta, final LocalizationReverseRouter reverseRouter) {
        meta.addHalLink(reverseRouter.processChangeLanguageForm(), "selectLanguage");
        meta.addHalLink(reverseRouter.processChangeCountryForm(), "selectCountry");
    }
}