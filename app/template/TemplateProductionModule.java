package template;

import com.google.inject.AbstractModule;
import common.template.cms.CmsService;
import common.template.engine.TemplateService;
import common.template.i18n.I18nResolver;

import javax.inject.Singleton;

/**
 * Configuration for the Guice {@link com.google.inject.Injector} which shall be used in production.
 */
public class TemplateProductionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(I18nResolver.class).toProvider(I18nResolverProvider.class).in(Singleton.class);
        bind(TemplateService.class).toProvider(TemplateServiceProvider.class).in(Singleton.class);
        bind(CmsService.class).toProvider(CmsServiceProvider.class).in(Singleton.class);
    }
}
