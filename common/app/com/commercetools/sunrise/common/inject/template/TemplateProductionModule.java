package com.commercetools.sunrise.common.inject.template;

import com.google.inject.AbstractModule;
import com.commercetools.sunrise.common.template.cms.CmsService;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;

import javax.inject.Singleton;

/**
 * Configuration for the Guice {@link com.google.inject.Injector} which shall be used in production.
 */
public class TemplateProductionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(I18nResolver.class).toProvider(I18nResolverProvider.class).in(Singleton.class);
        bind(CmsService.class).toProvider(CmsServiceProvider.class).in(Singleton.class);
        bind(TemplateEngine.class).toProvider(TemplateServiceProvider.class).in(Singleton.class);
    }
}
