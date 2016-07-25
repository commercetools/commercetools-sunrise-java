package com.commercetools.sunrise.common.template.engine.handlebars;

import com.commercetools.sunrise.common.template.engine.TemplateContext;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.common.template.engine.TemplateNotFoundException;
import com.commercetools.sunrise.common.template.engine.TemplateRenderException;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

public final class HandlebarsTemplateEngine implements TemplateEngine {

    private static final Logger logger = LoggerFactory.getLogger(HandlebarsTemplateEngine.class);
    static final String LANGUAGE_TAGS_IN_CONTEXT_KEY = "context-language-tags";
    static final String CMS_PAGE_IN_CONTEXT_KEY = "context-cms-page";
    private final Handlebars handlebars;

    private HandlebarsTemplateEngine(final Handlebars handlebars) {
        this.handlebars = handlebars;
    }

    @Override
    public String render(final String templateName, final TemplateContext templateContext) {
        final Template template = compileTemplate(templateName);
        final Context context = createContext(templateContext);
        try {
            logger.debug("Rendering template " + templateName);
            return template.apply(context);
        } catch (IOException e) {
            throw new TemplateRenderException("Context could not be applied to template " + templateName, e);
        }
    }

    private Context createContext(final TemplateContext templateContext) {
        final Context context = Context.newBuilder(templateContext.pageData())
                .resolver(
                        MapValueResolver.INSTANCE,
                        JavaBeanValueResolver.INSTANCE,
                        PlayJavaFormResolver.INSTANCE
                )
                .build();
        context.data(LANGUAGE_TAGS_IN_CONTEXT_KEY, templateContext.locales().stream().map(Locale::toLanguageTag).collect(toList()));
        templateContext.cmsPage()
                .ifPresent(cmsPage -> context.data(CMS_PAGE_IN_CONTEXT_KEY, cmsPage));
        return context;
    }

    private Template compileTemplate(final String templateName) {
        try {
            return handlebars.compile(templateName);
        } catch (IOException e) {
            throw new TemplateNotFoundException("Could not find the default template", e);
        }
    }

    public static TemplateEngine of(final Handlebars handlebars) {
        return new HandlebarsTemplateEngine(handlebars);
    }
}
