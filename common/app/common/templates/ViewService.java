package common.templates;

import common.pages.PageData;
import play.twirl.api.Html;

/**
 * Service that provides HTML pages, using some kind of template system.
 */
public interface ViewService {

    /**
     * Injects the page data into the template with the given name.
     * @param templateName name of the template
     * @param pageData data to be injected in the template
     * @return HTML generated with the template and the given page data
     */
    Html apply(final String templateName, final PageData pageData);
}
