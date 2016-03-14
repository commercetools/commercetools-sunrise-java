package common.cms;

import java.util.Locale;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Service that provides page content, usually coming from a Content Management System (CMS).
 * It expects a structure that can be accessed as explained below.
 *
 * To find the CMS Page:
 * - {@code pageKey} (e.g. home)
 *
 * To access the CMS Content:
 * - {@code contentType} (e.g. banner)
 * - {@code contentId} (e.g. homeTopLeft)
 *
 * To access the content field:
 * - {@code field} (e.g. subtitle)
 */
@FunctionalInterface
public interface CmsService {

    /**
     * Gets the page content corresponding to the given key for some certain languages.
     * @param locales for the localized text
     * @param pageKey identifying the page
     * @return the {@code completionStage} of the page contents identified by the key, in the given language
     * @throws CmsPageNotFoundException when the page identified by the given key could not be found in the given locale
     */
    CompletionStage<CmsPage> getPage(final List<Locale> locales, final String pageKey);
}
