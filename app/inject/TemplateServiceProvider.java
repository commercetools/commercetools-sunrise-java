package inject;

import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.google.inject.Provider;
import common.templates.HandlebarsTemplateService;
import common.templates.TemplateService;
import play.Logger;

public class TemplateServiceProvider implements Provider<TemplateService> {
    private static final String DEFAULT_TEMPLATE_PATH = "/META-INF/resources/webjars/templates";
    private static final String OVERRIDE_TEMPLATE_PATH = "app/assets/templates";

    @Override
    public TemplateService get() {
        Logger.debug("Provide HandlebarsTemplateService");
        return HandlebarsTemplateService.of(overrideLoader(), defaultLoader());
    }

    private ClassPathTemplateLoader defaultLoader() {
        return new ClassPathTemplateLoader(DEFAULT_TEMPLATE_PATH);
    }

    private FileTemplateLoader overrideLoader() {
        return new FileTemplateLoader(OVERRIDE_TEMPLATE_PATH);
    }
}
