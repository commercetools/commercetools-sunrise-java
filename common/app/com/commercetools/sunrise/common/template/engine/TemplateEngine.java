package com.commercetools.sunrise.common.template.engine;

import com.commercetools.sunrise.common.pages.PageData;
import play.twirl.api.Html;

import java.util.List;
import java.util.Locale;

/**
 * Service that provides HTML pages, using some sort of template engine.
 */
public interface TemplateEngine {

    /**
     * Injects the page data into the template with the given name.
     * @param templateName name of the template
     * @param pageData data to be injected in the template
     * @param locales locales used for i18n
     * @return string of the HTML generated with the template and the given page data
     * @throws TemplateNotFoundException when the given template name does not correspond to an existing template
     * @throws TemplateRenderException when the provided page data could not be injected to the template
     */
    String render(final String templateName, final PageData pageData, final List<Locale> locales);

    /**
     * Injects the page data into the template with the given name.
     * @param templateName name of the template
     * @param pageData data to be injected in the template
     * @param locales locales used for i18n
     * @return HTML generated with the template and the given page data
     * @throws TemplateNotFoundException when the given template name does not correspond to an existing template
     * @throws TemplateRenderException when the provided page data could not be injected to the template
     */
    @Deprecated
    default Html renderToHtml(final String templateName, final PageData pageData, final List<Locale> locales) {
        return new Html(render(templateName, pageData, locales));
    }
}
