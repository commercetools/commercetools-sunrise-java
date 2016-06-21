package com.commercetools.sunrise.common.template.engine;

import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.google.inject.Provider;
import com.commercetools.sunrise.common.SunriseInitializationException;
import com.commercetools.sunrise.common.template.cms.CmsService;
import com.commercetools.sunrise.common.template.engine.handlebars.HandlebarsTemplateEngine;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public final class TemplateEngineProvider implements Provider<TemplateEngine> {
    private static final Logger logger = LoggerFactory.getLogger(TemplateEngineProvider.class);
    private static final String CONFIG_TEMPLATE_LOADERS = "handlebars.templateLoaders";
    private static final String CLASSPATH_TYPE = "classpath";
    private static final String FILE_TYPE = "file";
    private static final String TYPE_ATTR = "type";
    private static final String PATH_ATTR = "path";

    @Inject
    private Configuration configuration;
    @Inject
    private I18nResolver i18NResolver;
    @Inject
    private CmsService cmsService;

    @Override
    public TemplateEngine get() {
        final List<TemplateLoader> templateLoaders = initializeTemplateLoaders(configuration, CONFIG_TEMPLATE_LOADERS);
        if (templateLoaders.isEmpty()) {
            throw new SunriseInitializationException("No Handlebars template loaders found in configuration '" + CONFIG_TEMPLATE_LOADERS + "'");
        }
        logger.info("Provide HandlebarsTemplateService: template loaders [{}]]",
                templateLoaders.stream().map(TemplateLoader::getPrefix).collect(joining(", ")));
        return HandlebarsTemplateEngine.of(templateLoaders, i18NResolver, cmsService);
    }

    private static List<TemplateLoader> initializeTemplateLoaders(final Configuration configuration, final String configKey) {
        return configuration.getConfigList(configKey, emptyList())
                .stream()
                .map(TemplateEngineProvider::initializeTemplateLoader)
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
