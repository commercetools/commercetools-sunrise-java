package com.commercetools.sunrise.core.renderers;

import com.commercetools.sunrise.core.renderers.handlebars.HandlebarsTemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.google.inject.ImplementedBy;
import play.twirl.api.Content;

import java.util.concurrent.CompletionStage;

/**
 * Service that provides HTML pages, using some sort of template engine.
 */
@ImplementedBy(HandlebarsTemplateEngine.class)
@FunctionalInterface
public interface TemplateEngine {

    /**
     * Injects the page data into the template with the given name.
     * @param templateName name of the template
     * @param pageData the data to be injected in the template
     * @return content of the HTML generated with the template and the given page data
     * @throws TemplateNotFoundException when the given template name does not correspond to an existing template
     * @throws TemplateRenderException when the provided page data could not be injected to the template
     */
    CompletionStage<Content> render(final String templateName, final PageData pageData);

    default CompletionStage<Content> render(final String templateName) {
        return render(templateName, PageData.of());
    }
}
