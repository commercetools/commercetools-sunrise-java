package common.i18n;

import common.cms.CmsPage;
import common.cms.CmsService;
import io.sphere.sdk.models.Base;
import play.Logger;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Resolves the i18n messages by getting them from the first CMS Page that succeeds.
 */
public final class CmsI18nResolver extends Base implements I18nResolver {
    private static final int TIMEOUT = 5000;
    private final CmsService cmsService;

    private CmsI18nResolver(final CmsService cmsService) {
        this.cmsService = cmsService;
        Logger.info("CMS i18n resolver loaded");
    }

    @Override
    public Optional<String> get(final List<Locale> locales, final String bundle, final String key, final Object... args) {
        for (final CmsPage cmsPage : cmsService.getPage(locales, bundle).get(TIMEOUT)) {
            final Optional<String> message = cmsPage.get(key, args);
            if (message.isPresent()) {
                return message;
            }
        }
        return Optional.empty();
    }

    public static CmsI18nResolver of(final CmsService cmsService) {
        return new CmsI18nResolver(cmsService);
    }
}
