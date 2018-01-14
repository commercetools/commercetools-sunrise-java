package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.cms.AbstractCmsComponent;
import com.commercetools.sunrise.cms.CmsService;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Locale;

public final class HomeCmsComponent extends AbstractCmsComponent {

    @Inject
    HomeCmsComponent(final CmsService cmsService, final Provider<Locale> localeProvider) {
        super(cmsService, localeProvider);
    }

    @Override
    protected String pageKey() {
        return "home";
    }
}
