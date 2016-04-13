package template;

import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.google.inject.Provider;
import common.template.i18n.I18nResolver;
import common.template.engine.TemplateService;
import common.template.engine.handlebars.HandlebarsTemplateService;
import common.SunriseInitializationException;
import play.Configuration;
import play.Logger;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class TemplateServiceProvider implements Provider<TemplateService> {
    private static final String CONFIG_TEMPLATE_LOADERS = "handlebars.templateLoaders";
    private static final String CLASSPATH_TYPE = "classpath";
    private static final String FILE_TYPE = "file";
    private static final String TYPE_ATTR = "type";
    private static final String PATH_ATTR = "path";
    private final Configuration configuration;
    private final I18nResolver i18NResolver;

    @Inject
    public TemplateServiceProvider(final Configuration configuration, final I18nResolver i18NResolver) {
        this.configuration = configuration;
        this.i18NResolver = i18NResolver;
    }

    @Override
    public TemplateService get() {
        final List<TemplateLoader> templateLoaders = initializeTemplateLoaders(configuration, CONFIG_TEMPLATE_LOADERS);
        if (templateLoaders.isEmpty()) {
            throw new SunriseInitializationException("No Handlebars template loaders found in configuration '" + CONFIG_TEMPLATE_LOADERS + "'");
        }
        Logger.info("Provide HandlebarsTemplateService: template loaders [{}]]",
                templateLoaders.stream().map(TemplateLoader::getPrefix).collect(joining(", ")));
        return HandlebarsTemplateService.of(templateLoaders, i18NResolver);
    }

    private static List<TemplateLoader> initializeTemplateLoaders(final Configuration configuration, final String configKey) {
        return configuration.getConfigList(configKey, emptyList())
                .stream()
                .map(TemplateServiceProvider::initializeTemplateLoader)
                .collect(toList());
    }

    private static TemplateLoader initializeTemplateLoader(final Configuration loaderConfig) {
        final String type = loaderConfig.getString(TYPE_ATTR);
        final String path = loaderConfig.getString(PATH_ATTR);
        switch (type) {
            case CLASSPATH_TYPE:
                return new ClassPathTemplateLoader(path);
            case FILE_TYPE:
                return new FileTemplateLoader(path);
            default:
                throw new SunriseInitializationException("Not recognized template loader: " + type);
        }
    }
}
