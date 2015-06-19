package common.templates;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.io.TemplateLoader;
import common.pages.PageData;
import play.Logger;
import play.twirl.api.Html;

import java.io.IOException;

public class HandlebarsViewService implements ViewService {
    private final Handlebars defaultTemplateSystem;
    private final Handlebars overrideTemplateSystem;

    private HandlebarsViewService(final Handlebars defaultTemplateSystem, final Handlebars overrideTemplateSystem) {
        this.defaultTemplateSystem = defaultTemplateSystem;
        this.overrideTemplateSystem = overrideTemplateSystem;
    }

    @Override
    public Html apply(final String templateName, final PageData pageData) {
        final Template template = compileTemplate(templateName);
        final Context context = buildContext(pageData);
        try {
            return new Html(template.apply(context));
        } catch (IOException e) {
            throw new TemplateException("Context could not be applied to template " + templateName, e);
        }
    }

    private Context buildContext(final PageData pageData) {
        return Context.newBuilder(pageData)
                .resolver(JavaBeanValueResolver.INSTANCE)
                .build();
    }

    private Template compileTemplate(final String templateName) {
        try {
            return overrideTemplateSystem.compile(templateName);
        } catch (IOException eOverride) {
            try {
                Logger.debug("Overridden template not found in " + eOverride.getMessage());
                return defaultTemplateSystem.compile(templateName);
            } catch (IOException eDefault) {
                throw new TemplateException("Could not find the default template", eDefault);
            }
        }
    }

    public static HandlebarsViewService of(final TemplateLoader defaultLoader, final TemplateLoader overrideLoader) {
        final Handlebars defaultTemplateSystem = new Handlebars(defaultLoader);
        final Handlebars overrideTemplateSystem = new Handlebars(overrideLoader);
        return new HandlebarsViewService(defaultTemplateSystem, overrideTemplateSystem);
    }
}
