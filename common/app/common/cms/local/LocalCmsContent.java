package common.cms.local;

import common.cms.CmsContent;
import common.i18n.I18nResolver;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

final class LocalCmsContent implements CmsContent {
    private final I18nResolver i18nResolver;
    private final List<Locale> locales;
    private final String contentType;
    private final String contentId;

    LocalCmsContent(final I18nResolver i18nResolver, final List<Locale> locales, final String contentType, final String contentId) {
        this.i18nResolver = i18nResolver;
        this.locales = locales;
        this.contentType = contentType;
        this.contentId = contentId;
    }

    @Override
    public Optional<String> get(final String field, final Map<String, Object> hashArgs) {
        if (field.isEmpty()) {
            return Optional.empty();
        } else {
            final String messageKey = messageKey(field);
            return i18nResolver.get(locales, contentType, messageKey, hashArgs);
        }
    }

    /**
     * Generates the message key as {@code contentId.field} (e.g. homeTopLeft.subtitle).
     * @param field the content field to access
     * @return the message key mapped to the content ID and field
     */
    private String messageKey(final String field) {
        return contentId + "." + field;
    }
}
