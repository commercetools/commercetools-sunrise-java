package template;

import com.google.inject.AbstractModule;
import common.template.cms.CmsService;
import common.template.i18n.I18nResolver;
import common.template.engine.TemplateService;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class TemplateTestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(I18nResolver.class).toInstance(injectedI18nResolver());
        bind(TemplateService.class).toInstance(injectedTemplateService());
        bind(CmsService.class).toInstance(injectedCmsService());
    }

    private I18nResolver injectedI18nResolver() {
        return ((locales, i18nIdentifier, hashArgs) -> Optional.empty());
    }

    protected TemplateService injectedTemplateService() {
        return ((templateName, pageData, locales, cmsPage) -> "");
    }

    protected CmsService injectedCmsService() {
        return ((locales, pageKey) -> CompletableFuture.completedFuture((messageKey, args) -> Optional.empty()));
    }
}
