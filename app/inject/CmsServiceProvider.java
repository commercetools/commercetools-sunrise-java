package inject;

import com.google.inject.Provider;
import common.cms.CmsService;
import common.cms.local.LocalCmsService;
import common.i18n.I18nResolver;
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
