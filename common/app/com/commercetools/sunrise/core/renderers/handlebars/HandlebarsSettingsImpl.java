package com.commercetools.sunrise.core.renderers.handlebars;

import com.commercetools.sunrise.play.configuration.SunriseConfigurationException;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import io.sphere.sdk.models.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Singleton
final class HandlebarsSettingsImpl extends Base implements HandlebarsSettings {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlebarsSettings.class);

    private final List<TemplateLoader> templateLoaders;

    @Inject
    HandlebarsSettingsImpl(final Configuration configuration) {
        this(configuration, "handlebars");
    }

    HandlebarsSettingsImpl(final Configuration globalConfig, final String configPath) {
        final Configuration config = globalConfig.getConfig(configPath);
        this.templateLoaders = config.getConfigList("templateLoaders", emptyList()).stream()
                .map(HandlebarsSettingsImpl::initializeTemplateLoader)
                .collect(toList());
        if (templateLoaders.isEmpty()) {
            throw new SunriseConfigurationException("Missing Handlebars template loaders configuration");
        } else {
            LOGGER.info("Configured Handlebars template loaders: [{}]]",
                    templateLoaders.stream().map(TemplateLoader::getPrefix).collect(joining(", ")));
        }
    }

    @Override
    public List<TemplateLoader> templateLoaders() {
        return templateLoaders;
    }

    private static TemplateLoader initializeTemplateLoader(final Configuration loaderConfig) {
        final String type = loaderConfig.getString("type");
        final String path = loaderConfig.getString("path");
        switch (type) {
            case "classpath":
                return new ClassPathTemplateLoader(path);
            case "file":
                return new FileTemplateLoader(path);
            default:
                throw new SunriseConfigurationException("Unrecognized Handlebars template loader \"" + type + "\"");
        }
    }
}
