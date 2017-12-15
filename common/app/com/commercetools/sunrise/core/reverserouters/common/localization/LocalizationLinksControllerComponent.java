package com.commercetools.sunrise.core.reverserouters.common.localization;

import com.commercetools.sunrise.core.reverserouters.AbstractLinksControllerComponent;
import com.commercetools.sunrise.core.viewmodels.meta.PageMeta;

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
        meta.addHalLink(reverseRouter.changeLanguageProcessCall(), "selectLanguage");
        meta.addHalLink(reverseRouter.changeCountryProcessCall(), "selectCountry");
    }
}