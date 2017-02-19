package com.commercetools.sunrise.framework.template.engine.handlebars;

import com.commercetools.sunrise.framework.template.engine.TemplateContext;
import com.commercetools.sunrise.framework.template.engine.TemplateEngine;
import com.commercetools.sunrise.framework.template.engine.TemplateNotFoundException;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderException;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class HandlebarsTemplateEngine implements TemplateEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateEngine.class);
    private final Handlebars handlebars;
    private final HandlebarsContextFactory contextFactory;

    private HandlebarsTemplateEngine(final Handlebars handlebars, final HandlebarsContextFactory contextFactory) {
        this.handlebars = handlebars;
        this.contextFactory = contextFactory;
    }

    @Override
    public String render(final String templateName, final TemplateContext templateContext) {
        final Template template = compileTemplate(templateName);
        final Context context = contextFactory.create(handlebars, templateName, templateContext);
        try {
            LOGGER.debug("Rendering template " + templateName);
            return template.apply(context);
        } catch (IOException e) {
            throw new TemplateRenderException("Context could not be applied to template " + templateName, e);
        }
    }

    private Template compileTemplate(final String templateName) {
        try {
            return handlebars.compile(templateName);
        } catch (IOException e) {
            throw new TemplateNotFoundException("Could not find the default template", e);
        }
    }

    public static TemplateEngine of(final Handlebars handlebars, final HandlebarsContextFactory contextFactory) {
        return new HandlebarsTemplateEngine(handlebars, contextFactory);
    }
}
