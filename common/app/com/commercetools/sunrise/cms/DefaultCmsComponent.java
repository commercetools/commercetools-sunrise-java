package com.commercetools.sunrise.cms;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Locale;

public final class DefaultCmsComponent extends AbstractCmsComponent {

    @Inject
    DefaultCmsComponent(final CmsService cmsService, final Provider<Locale> localeProvider) {
        super(cmsService, localeProvider);
    }

    @Override
    protected String pageKey() {
        return "default";
    }
}
