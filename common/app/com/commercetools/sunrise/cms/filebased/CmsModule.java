package com.commercetools.sunrise.cms.filebased;

import com.commercetools.sunrise.cms.CmsService;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public final class CmsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CmsService.class)
                .to(FileBasedCmsService.class)
                .in(Singleton.class);
    }
}
