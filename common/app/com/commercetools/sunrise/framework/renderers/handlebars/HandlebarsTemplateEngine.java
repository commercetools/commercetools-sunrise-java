package com.commercetools.sunrise.framework.renderers.handlebars;

import com.commercetools.sunrise.framework.renderers.TemplateContext;
import com.commercetools.sunrise.framework.renderers.TemplateEngine;
import com.commercetools.sunrise.framework.renderers.TemplateNotFoundException;
import com.commercetools.sunrise.framework.renderers.TemplateRenderException;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.ValueResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class HandlebarsTemplateEngine implements TemplateEngine {

    public static final String CMS_PAGE_IN_CONTEXT_KEY = "context-cms-page";
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateEngine.class);

    private final Handlebars handlebars;
    private final HandlebarsValueResolvers handlebarsValueResolvers;

    @Inject
    protected HandlebarsTemplateEngine(final Handlebars handlebars, final HandlebarsValueResolvers handlebarsValueResolvers) {
        this.handlebars = handlebars;
        this.handlebarsValueResolvers = handlebarsValueResolvers;
    }

    @Override
    public String render(final String templateName, final TemplateContext templateContext) {
        final Template template = compileTemplate(templateName);
        final Context context = createContext(templateContext);
        try {
            LOGGER.debug("Rendering template " + templateName);
            return template.apply(context);
        } catch (IOException e) {
            throw new TemplateRenderException("Context could not be applied to template " + templateName, e);
        }
    }

    protected Context createContext(final TemplateContext templateContext) {
        final Map<String, Object> attributes = contextAttributes(templateContext);
        return Context.newBuilder(templateContext.pageData())
                .resolver(valueResolvers())
                .build()
                .data(attributes);
    }

    private Template compileTemplate(final String templateName) {
        try {
            return handlebars.compile(templateName);
        } catch (IOException e) {
            throw new TemplateNotFoundException("Could not find the default template", e);
        }
    }

    private Map<String, Object> contextAttributes(final TemplateContext templateContext) {
        final Map<String, Object> attributes = new HashMap<>();
        templateContext.cmsPage().ifPresent(cmsPage -> attributes.put(CMS_PAGE_IN_CONTEXT_KEY, cmsPage));
        return attributes;
    }

    private ValueResolver[] valueResolvers() {
        final List<ValueResolver> valueResolvers = handlebarsValueResolvers.valueResolvers();
        return valueResolvers.toArray(new ValueResolver[valueResolvers.size()]);
    }
}
