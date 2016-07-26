package com.commercetools.sunrise.common.template;

import com.commercetools.sunrise.cms.CmsService;
import com.commercetools.sunrise.common.template.cms.filebased.CmsI18nResolverProvider;
import com.commercetools.sunrise.common.template.cms.filebased.FileBasedCmsService;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.common.template.engine.TemplateEngineProvider;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.common.template.i18n.I18nResolverProvider;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import javax.inject.Singleton;

/**
 * Configuration for the Guice {@link com.google.inject.Injector} which shall be used in production.
 */
public class TemplateModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(I18nResolver.class).annotatedWith(Names.named("cms")).toProvider(CmsI18nResolverProvider.class).in(Singleton.class);
        bind(I18nResolver.class).toProvider(I18nResolverProvider.class).in(Singleton.class);
        bind(TemplateEngine.class).toProvider(TemplateEngineProvider.class).in(Singleton.class);
        bind(CmsService.class).to(FileBasedCmsService.class);
    }
}
