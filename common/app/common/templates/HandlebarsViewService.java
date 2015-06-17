package common.templates;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import common.pages.PageData;
import play.Logger;
import play.twirl.api.Html;

import java.io.IOException;

public abstract class HandlebarsViewService {
    private static final String DEFAULT_TEMPLATE_PATH = "/META-INF/resources/webjars/templates";
    private static final String OVERRIDE_TEMPLATE_PATH = "app/assets/templates";

    private Handlebars defaultTemplateSystem;
    private Handlebars overrideTemplateSystem;

    protected HandlebarsViewService() {
        this.defaultTemplateSystem = new Handlebars(new ClassPathTemplateLoader(DEFAULT_TEMPLATE_PATH));
        this.overrideTemplateSystem = new Handlebars(new FileTemplateLoader(OVERRIDE_TEMPLATE_PATH));
    }

    protected Html html(final String templateName, final PageData pageData) {
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
}
