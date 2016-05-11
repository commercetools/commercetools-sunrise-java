package common.template.cms.filebased;

import common.template.cms.CmsContent;
import common.template.cms.CmsPage;
import common.template.i18n.I18nResolver;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

final class FileBasedCmsPage implements CmsPage {
    private final I18nResolver i18nResolver;
    private final List<Locale> locales;

    FileBasedCmsPage(final I18nResolver i18nResolver, final List<Locale> locales) {
        this.i18nResolver = i18nResolver;
        this.locales = locales;
    }

    @Override
    public Optional<CmsContent> get(final String contentType, final String contentId) {
        if (contentType.isEmpty() || contentId.isEmpty()) {
            return Optional.empty();
        } else {
            final FileBasedCmsContent cmsContent = new FileBasedCmsContent(i18nResolver, locales, contentType, contentId);
            return Optional.of(cmsContent);
        }
    }
}
