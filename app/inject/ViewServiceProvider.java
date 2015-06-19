package inject;

import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.google.inject.Provider;
import common.templates.HandlebarsViewService;
import common.templates.ViewService;
import play.Logger;

public class ViewServiceProvider implements Provider<ViewService> {
    private static final String DEFAULT_TEMPLATE_PATH = "/META-INF/resources/webjars/templates";
    private static final String OVERRIDE_TEMPLATE_PATH = "app/assets/templates";

    @Override
    public ViewService get() {
        Logger.info("execute ViewServiceProvider.get()");
        return HandlebarsViewService.of(defaultLoader(), overrideLoader());
    }

    private ClassPathTemplateLoader defaultLoader() {
        return new ClassPathTemplateLoader(DEFAULT_TEMPLATE_PATH);
    }

    private FileTemplateLoader overrideLoader() {
        return new FileTemplateLoader(OVERRIDE_TEMPLATE_PATH);
    }
}
