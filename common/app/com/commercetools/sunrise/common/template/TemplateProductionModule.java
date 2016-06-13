package com.commercetools.sunrise.common.template;

import com.commercetools.sunrise.common.template.cms.CmsService;
import com.commercetools.sunrise.common.template.cms.filebased.FileBasedCmsService;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.common.template.engine.TemplateEngineProvider;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.common.template.i18n.I18nResolverProvider;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

/**
 * Configuration for the Guice {@link com.google.inject.Injector} which shall be used in production.
 */
public class TemplateProductionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(I18nResolver.class).toProvider(I18nResolverProvider.class).in(Singleton.class);
        bind(TemplateEngine.class).toProvider(TemplateEngineProvider.class).in(Singleton.class);
        bind(CmsService.class).to(FileBasedCmsService.class);
    }
}
