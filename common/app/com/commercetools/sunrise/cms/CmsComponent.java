package com.commercetools.sunrise.cms;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Locale;

public final class CmsComponent extends AbstractCmsComponent {

    @Inject
    CmsComponent(final CmsService cmsService, final Provider<Locale> localeProvider) {
        super(cmsService, localeProvider);
    }

    @Override
    protected String pageKey() {
        return "default";
    }
}
