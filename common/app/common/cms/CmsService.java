package common.cms;

import play.libs.F;

import java.util.List;
import java.util.Locale;

/**
 * Service that provides page content, usually coming from a Content Management System (CMS).
 */
@FunctionalInterface
public interface CmsService {

    /**
     * Gets the page content corresponding to the given key for some certain languages.
     * @param locales for the localized text
     * @param pageKey identifying the page
     * @return the promise of the page contents identified by the key, in the given language
     * @throws CmsPageNotFoundException when the page identified by the given key could not be found in the given locale
     */
    F.Promise<List<CmsPage>> getPage(final List<Locale> locales, final String pageKey);
}
