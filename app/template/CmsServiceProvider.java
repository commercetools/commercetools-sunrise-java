package template;

import com.google.inject.Provider;
import common.template.cms.CmsService;
import common.template.cms.local.LocalCmsService;
import common.template.i18n.I18nResolver;
import play.Logger;

import javax.inject.Inject;

class CmsServiceProvider implements Provider<CmsService> {
    private final I18nResolver i18nResolver;

    @Inject
    private CmsServiceProvider(final I18nResolver i18nResolver) {
        this.i18nResolver = i18nResolver;
    }

    @Override
    public CmsService get() {
        Logger.info("Provide LocalCmsService");
        return LocalCmsService.of(i18nResolver);
    }
}
