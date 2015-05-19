package generalpages.controllers;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import play.Logger;
import play.Play;

import java.io.IOException;

public class TemplateService {
    private static final String DEFAULT_TEMPLATE_PATH = "/templates";
    private static final String OVERRIDE_TEMPLATE_PATH = Play.application().path() + "/app/assets/templates";

    private Handlebars defaultTemplateSystem;
    private Handlebars overrideTemplateSystem;

    private TemplateService(final Handlebars defaultTemplateSystem, final Handlebars overrideTemplateSystem) {
        this.defaultTemplateSystem = defaultTemplateSystem;
        this.overrideTemplateSystem = overrideTemplateSystem;
    }

    public Template compile(final String templateName) {
        try {
            return overrideTemplateSystem.compile(templateName);
        } catch (IOException eOverride) {
            try {
                Logger.debug("Overridden template not found in " + eOverride.getMessage());
                return defaultTemplateSystem.compile(templateName);
            } catch (IOException eDefault) {
                throw new RuntimeException("Could not find the default template", eDefault);
            }
        }
    }

    public static TemplateService of() {
        final Handlebars defaultTemplateSystem = new Handlebars(new ClassPathTemplateLoader(DEFAULT_TEMPLATE_PATH));
        final Handlebars overrideTemplateSystem = new Handlebars(new FileTemplateLoader(OVERRIDE_TEMPLATE_PATH));
        return new TemplateService(defaultTemplateSystem, overrideTemplateSystem);
    }
}
