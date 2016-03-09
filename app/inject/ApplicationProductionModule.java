package inject;

import com.google.inject.AbstractModule;
import common.cms.CmsService;
import common.i18n.I18nResolver;
import common.templates.TemplateService;

import javax.inject.Singleton;

/**
 * Configuration for the Guice {@link com.google.inject.Injector} which shall be used in production.
 */
public class ApplicationProductionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(I18nResolver.class).toProvider(I18nResolverProvider.class).in(Singleton.class);
        bind(TemplateService.class).toProvider(TemplateServiceProvider.class).in(Singleton.class);
        bind(CmsService.class).toProvider(CmsServiceProvider.class);
    }
}
