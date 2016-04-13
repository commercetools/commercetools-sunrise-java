package common.template.cms;

import java.util.Optional;

/**
 * Obtains content data for a page, usually coming from a Content Management System (CMS).
 *
 * @see CmsService
 */
@FunctionalInterface
public interface CmsPage {

    /**
     * Gets the message corresponding to the given key, or empty in case it does not exist.
     * @param contentType type of the content
     * @param contentId identification of the content
     * @return the content of a particular type and ID, or absent if not found
     */
    Optional<CmsContent> get(final String contentType, final String contentId);

}
