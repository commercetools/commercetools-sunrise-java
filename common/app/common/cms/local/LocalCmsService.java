package common.cms.local;

import common.cms.CmsPage;
import common.cms.CmsService;
import common.i18n.I18nResolver;
import play.libs.F;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;

/**
 * Service that provides content data from i18n files that can be found locally.
 * It uses a I18nResolver internally to resolve the requested messages.
 *
 * The mapping of the {@link CmsService} parameters to {@link I18nResolver} parameters goes as follows:
 *
 * - {@code bundle} = {@code contentType} (e.g. banner)
 * - {@code key} = {@code contentId.field} (e.g. homeTopLeft.subtitle)
 *
 * The {@code pageKey} is then ignored as its only purpose is for remote access.
 */
@Singleton
public final class LocalCmsService implements CmsService {
    private final I18nResolver i18nResolver;

    @Inject
    private LocalCmsService(final I18nResolver i18nResolver) {
        this.i18nResolver = i18nResolver;
    }

    @Override
    public F.Promise<CmsPage> getPage(final List<Locale> locales, final String pageKey) {
        final CmsPage cmsPage = new LocalCmsPage(i18nResolver, locales);
        return F.Promise.pure(cmsPage);
    }

    public static LocalCmsService of(final I18nResolver i18nResolver) {
        return new LocalCmsService(i18nResolver);
    }
}