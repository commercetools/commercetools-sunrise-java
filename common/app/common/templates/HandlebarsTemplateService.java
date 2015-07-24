package common.templates;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.TemplateLoader;
import common.pages.PageData;
import play.Logger;

import java.io.IOException;
import java.util.List;

public final class HandlebarsTemplateService implements TemplateService {
    private final Handlebars handlebars;

    private HandlebarsTemplateService(final Handlebars handlebars) {
        this.handlebars = handlebars;
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

    public static HandlebarsTemplateService of(final TemplateLoader... loaders) {
        final Handlebars handlebars = new Handlebars().with(loaders);
        return new HandlebarsTemplateService(handlebars);
    }

    private Template compileTemplate(final String templateName) {
        try {
            return handlebars.compile(templateName);
        } catch (IOException e) {
            throw new TemplateNotFoundException("Could not find the default template", e);
        }
    }

    private Context buildContext(final PageData pageData) {
        // TODO Use resolver with cache on production
        return Context.newBuilder(pageData)
                .resolver(NonCachedJavaBeanValueResolver.INSTANCE)
                .build();
    }


    public static TemplateService of(final List<TemplateLoader> templateLoaders) {
        final TemplateLoader[] templateLoadersArray =
                templateLoaders.toArray(new TemplateLoader[templateLoaders.size()]);
        return of(templateLoadersArray);
    }
}
