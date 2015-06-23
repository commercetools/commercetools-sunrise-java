package common.templates;

import common.pages.PageData;
import play.twirl.api.Html;

/**
 * Service that provides HTML pages, using some kind of template system.
 */
public interface TemplateService {

    /**
     * Injects the page data into the template with the given name.
     * @param templateName name of the template
     * @param pageData data to be injected in the template
     * @return string of the HTML generated with the template and the given page data
     * @throws TemplateNotFoundException when the given template name does not correspond to an existing template
     * @throws TemplateRenderException when the provided page data could not be injected to the template
     */
    String render(final String templateName, final PageData pageData);

    /**
     * Injects the page data into the template with the given name.
     * @param templateName name of the template
     * @param pageData data to be injected in the template
     * @return HTML generated with the template and the given page data
     * @throws TemplateNotFoundException when the given template name does not correspond to an existing template
     * @throws TemplateRenderException when the provided page data could not be injected to the template
     */
    default Html renderToHtml(final String templateName, final PageData pageData) {
        return new Html(render(templateName, pageData));
    }
}
