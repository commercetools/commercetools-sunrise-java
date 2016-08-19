import com.commercetools.sunrise.cms.CmsService;
import com.commercetools.sunrise.common.httpauth.HttpAuthentication;
import com.commercetools.sunrise.common.httpauth.basic.BasicAuthenticationProvider;
import com.commercetools.sunrise.common.template.cms.FileBasedCmsServiceProvider;
import com.commercetools.sunrise.common.template.engine.HandlebarsTemplateEngineProvider;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.common.template.i18n.ConfigurableI18nResolverProvider;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class Module extends AbstractModule {

    @Override
    protected void configure() {
        bind(CmsService.class).toProvider(FileBasedCmsServiceProvider.class).in(Singleton.class);
        bind(TemplateEngine.class).toProvider(HandlebarsTemplateEngineProvider.class).in(Singleton.class);
        bind(I18nResolver.class).toProvider(ConfigurableI18nResolverProvider.class).in(Singleton.class);
        bind(HttpAuthentication.class).toProvider(BasicAuthenticationProvider.class).in(Singleton.class);
    }
}
