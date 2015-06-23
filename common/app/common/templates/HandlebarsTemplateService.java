package common.templates;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.TemplateLoader;
import common.pages.PageData;
import play.Logger;

import java.io.IOException;
import java.util.Optional;

public final class HandlebarsTemplateService implements TemplateService {
    private final Handlebars defaultTemplateSystem;
    private final Optional<Handlebars> overrideTemplateSystem;

    private HandlebarsTemplateService(final Handlebars defaultTemplateSystem, final Optional<Handlebars> overrideTemplateSystem) {
        this.defaultTemplateSystem = defaultTemplateSystem;
        this.overrideTemplateSystem = overrideTemplateSystem;
    }

    @Override
    public String render(final String templateName, final PageData pageData) {
        final Template template = compileTemplate(templateName);
        final Context context = buildContext(pageData);
        try {
            Logger.debug("Rendering template " + templateName);
            return template.apply(context);
        } catch (IOException e) {
            throw new TemplateRenderException("Context could not be applied to template " + templateName, e);
        }
    }

    public static HandlebarsTemplateService of(final TemplateLoader defaultLoader) {
        return of(defaultLoader, null);
    }

    public static HandlebarsTemplateService of(final TemplateLoader defaultLoader, final TemplateLoader overrideLoader) {
        final Handlebars defaultTemplateSystem = new Handlebars(defaultLoader);
        final Optional<Handlebars> overrideTemplateSystem = Optional.ofNullable(overrideLoader).map(Handlebars::new);
        return new HandlebarsTemplateService(defaultTemplateSystem, overrideTemplateSystem);
    }

    private Context buildContext(final PageData pageData) {
        // TODO Use resolver with cache on production
        return Context.newBuilder(pageData)
                .resolver(NonCachedJavaBeanValueResolver.INSTANCE)
                .build();
    }

    private Template compileTemplate(final String templateName) {
        return compileOverrideTemplate(templateName).orElse(compileDefaultTemplate(templateName));
    }

    private Optional<Template> compileOverrideTemplate(final String templateName) {
        return overrideTemplateSystem.flatMap(templateSystem -> {
            try {
                return Optional.of(templateSystem.compile(templateName));
            } catch (IOException e) {
                Logger.debug("Overridden template not found in " + e.getMessage());
                return Optional.empty();
            }
        });
    }

    private Template compileDefaultTemplate(final String templateName) {
        try {
            return defaultTemplateSystem.compile(templateName);
        } catch (IOException e) {
            throw new TemplateNotFoundException("Could not find the default template", e);
        }
    }
}
