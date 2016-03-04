package inject;

import com.google.inject.AbstractModule;
import common.cms.CmsService;
import common.i18n.I18nResolver;
import common.templates.TemplateService;
import play.libs.F;

import java.util.Optional;

public class ApplicationTestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(I18nResolver.class).toInstance(injectedI18nResolver());
        bind(TemplateService.class).toInstance(injectedTemplateService());
        bind(CmsService.class).toInstance(injectedCmsService());
    }

    private I18nResolver injectedI18nResolver() {
        return ((locales, bundle, key, args) -> Optional.empty());
    }

    protected TemplateService injectedTemplateService() {
        return ((templateName, pageData, locales) -> "");
    }

    protected CmsService injectedCmsService() {
        return ((locales, pageKey) -> F.Promise.pure((messageKey, args) -> Optional.empty()));
    }
}
