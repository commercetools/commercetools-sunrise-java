package template;

import com.google.inject.AbstractModule;
import common.template.cms.CmsService;
import common.template.engine.TemplateEngine;
import common.template.i18n.I18nResolver;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class TemplateTestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(I18nResolver.class).toInstance(injectedI18nResolver());
        bind(TemplateEngine.class).toInstance(injectedTemplateService());
        bind(CmsService.class).toInstance(injectedCmsService());
    }

    private I18nResolver injectedI18nResolver() {
        return ((locales, i18nIdentifier, hashArgs) -> Optional.empty());
    }

    protected TemplateEngine injectedTemplateService() {
        return ((templateName, pageData, locales) -> "");
    }

    protected CmsService injectedCmsService() {
        return ((locales, cmsIdentifier) -> CompletableFuture.completedFuture(Optional.empty()));
    }
}
