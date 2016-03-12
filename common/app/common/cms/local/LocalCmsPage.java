package common.cms.local;

import common.cms.CmsContent;
import common.cms.CmsPage;
import common.i18n.I18nResolver;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

final class LocalCmsPage implements CmsPage {
    private final I18nResolver i18nResolver;
    private final List<Locale> locales;

    LocalCmsPage(final I18nResolver i18nResolver, final List<Locale> locales) {
        this.i18nResolver = i18nResolver;
        this.locales = locales;
    }

    @Override
    public Optional<CmsContent> get(final String contentType, final String contentId) {
        if (contentType.isEmpty() || contentId.isEmpty()) {
            return Optional.empty();
        } else {
            final LocalCmsContent cmsContent = new LocalCmsContent(i18nResolver, locales, contentType, contentId);
            return Optional.of(cmsContent);
        }
    }
}
