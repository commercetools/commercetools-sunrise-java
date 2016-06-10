package com.commercetools.sunrise.common.inject.template;

import com.google.inject.Provider;
import com.commercetools.sunrise.common.template.cms.CmsService;
import com.commercetools.sunrise.common.template.cms.filebased.FileBasedCmsService;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import play.Logger;

import javax.inject.Inject;

class CmsServiceProvider implements Provider<CmsService> {
    private final I18nResolver i18nResolver;

    @Inject
    private CmsServiceProvider(final I18nResolver i18nResolver) {
        this.i18nResolver = i18nResolver;
    }

    @Override
    public CmsService get() {
        Logger.info("Provide FileBasedCmsService");
        return FileBasedCmsService.of(i18nResolver);
    }
}
