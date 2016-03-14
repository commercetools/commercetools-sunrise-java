package common.cms;

import java.util.Locale;
import java.util.concurrent.CompletionStage;

/**
 * Service that provides page content, usually coming from a Content Management System (CMS).
 */
public interface CmsService {

    /**
     * Gets the page content corresponding to the given key for a certain language.
     * @param locale for the localized text
     * @param pageKey identifying the page
     * @return the completionStage of the page content identified by the key, in the given language
     * @throws CmsPageNotFoundException when the page identified by the given key could not be found in the given locale
     */
    CompletionStage<CmsPage> getPage(final Locale locale, final String pageKey);
}
