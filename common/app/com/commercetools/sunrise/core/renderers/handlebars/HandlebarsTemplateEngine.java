package com.commercetools.sunrise.core.renderers.handlebars;

import com.commercetools.sunrise.core.hooks.HookContext;
import com.commercetools.sunrise.core.hooks.application.HandlebarsHook;
import com.commercetools.sunrise.core.renderers.AbstractTemplateEngine;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.renderers.TemplateNotFoundException;
import com.commercetools.sunrise.core.renderers.TemplateRenderException;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.ValueResolver;
import play.libs.concurrent.HttpExecution;
import play.twirl.api.Content;
import play.twirl.api.Html;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Singleton
public class HandlebarsTemplateEngine extends AbstractTemplateEngine implements TemplateEngine {

    private final Handlebars handlebars;
    private final HandlebarsValueResolvers handlebarsValueResolvers;

    @Inject
    protected HandlebarsTemplateEngine(final Handlebars handlebars,
                                       final HandlebarsValueResolvers handlebarsValueResolvers,
                                       final HookContext hookContext) {
        super(hookContext);
        this.handlebars = handlebars;
        this.handlebarsValueResolvers = handlebarsValueResolvers;
    }

    @Override
    public CompletionStage<Content> render(final String templateName, final PageData pageData) {
        return compileTemplate(templateName)
                .thenComposeAsync(template -> applyPageDataHooks(pageData)
                        .thenApply(this::createContext)
                        .thenApply(context -> renderHtml(template, context)), HttpExecution.defaultContext());
    }

    protected Context createContext(final PageData pageData) {
        return Context.newBuilder(pageData)
                .resolver(valueResolvers())
                .build();
    }

    private CompletionStage<Template> compileTemplate(final String templateName) {
        return HandlebarsHook.runHook(getHookContext(), handlebars).thenApply(finalHandlebars -> {
            try {
                return finalHandlebars.compile(templateName);
            } catch (IOException e) {
                throw new TemplateNotFoundException("Could not find the default template", e);
            }
        });
    }

    private Content renderHtml(final Template template, final Context context) {
        try {
            LOGGER.debug("Rendering template " + template.filename());
            final String html = template.apply(context);
            return new Html(html);
        } catch (IOException e) {
            throw new TemplateRenderException("Context could not be applied to template " + template.filename(), e);
        }
    }

    private ValueResolver[] valueResolvers() {
        final List<ValueResolver> valueResolvers = handlebarsValueResolvers.valueResolvers();
        return valueResolvers.toArray(new ValueResolver[valueResolvers.size()]);
    }
}
