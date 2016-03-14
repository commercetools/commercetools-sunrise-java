package inject;

import com.google.inject.AbstractModule;
import common.cms.CmsService;
import common.i18n.I18nResolver;
import common.templates.TemplateService;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ApplicationTestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(I18nResolver.class).toInstance(injectedI18nResolver());
        bind(TemplateService.class).toInstance(injectedTemplateService());
        bind(CmsService.class).toInstance(injectedCmsService());
    }

    private I18nResolver injectedI18nResolver() {
        return ((locale, bundle, key, args) -> Optional.empty());
    }

    protected TemplateService injectedTemplateService() {
        return ((templateName, pageData, locales) -> "");
    }

    protected CmsService injectedCmsService() {
        return ((locale, pageKey) -> CompletableFuture.completedFuture((messageKey, args) -> Optional.empty()));
    }
}
