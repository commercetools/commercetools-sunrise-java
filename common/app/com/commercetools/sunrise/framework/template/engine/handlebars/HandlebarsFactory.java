package com.commercetools.sunrise.framework.template.engine.handlebars;

import com.commercetools.sunrise.play.configuration.SunriseConfigurationException;
import com.commercetools.sunrise.framework.SunriseModel;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.framework.template.i18n.I18nResolver;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.cache.HighConcurrencyTemplateCache;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class HandlebarsFactory extends SunriseModel {

    private static final Logger logger = LoggerFactory.getLogger(HandlebarsFactory.class);
    private static final String CONFIG_TEMPLATE_LOADERS = "templateLoaders";
    private static final String CLASSPATH_TYPE = "classpath";
    private static final String FILE_TYPE = "file";
    private static final String TYPE_ATTR = "type";
    private static final String PATH_ATTR = "path";

    private final I18nResolver i18NResolver;
    private final I18nIdentifierFactory i18nIdentifierFactory;

    @Inject
    public HandlebarsFactory(final I18nResolver i18NResolver, final I18nIdentifierFactory i18nIdentifierFactory) {
        this.i18NResolver = i18NResolver;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
    }

    public Handlebars create(final Configuration configuration) {
        final List<TemplateLoader> templateLoaders = initializeTemplateLoaders(configuration, CONFIG_TEMPLATE_LOADERS);
        return create(templateLoaders, i18NResolver, i18nIdentifierFactory);
    }

    //for testing package scope
    static Handlebars create(final List<TemplateLoader> templateLoaders, final I18nResolver i18NResolver, final I18nIdentifierFactory i18nIdentifierFactory) {
        if (templateLoaders.isEmpty()) {
            throw new SunriseConfigurationException("Could not initialize Handlebars due to missing template loaders configuration", CONFIG_TEMPLATE_LOADERS);
        }
        logger.info("Provide Handlebars: template loaders [{}]]",
                templateLoaders.stream().map(TemplateLoader::getPrefix).collect(joining(", ")));
        final TemplateLoader[] loaders = templateLoaders.toArray(new TemplateLoader[templateLoaders.size()]);
        final Handlebars handlebars = new Handlebars()
                .with(loaders)
                .with(new HighConcurrencyTemplateCache())
                .infiniteLoops(true);
        handlebars.registerHelper("i18n", new HandlebarsI18nHelper(i18NResolver, i18nIdentifierFactory));
        handlebars.registerHelper("cms", new HandlebarsCmsHelper());
        handlebars.registerHelper("json", new HandlebarsJsonHelper<>());
        return handlebars;
    }

    protected static List<TemplateLoader> initializeTemplateLoaders(final Configuration configuration, final String configKey) {
        return configuration.getConfigList(configKey, emptyList())
                .stream()
                .map(HandlebarsFactory::initializeTemplateLoader)
                .collect(toList());
    }

    protected static TemplateLoader initializeTemplateLoader(final Configuration loaderConfig) {
        final String type = loaderConfig.getString(TYPE_ATTR);
        final String path = loaderConfig.getString(PATH_ATTR);
        switch (type) {
            case CLASSPATH_TYPE:
                return new ClassPathTemplateLoader(path);
            case FILE_TYPE:
                return new FileTemplateLoader(path);
            default:
                throw new SunriseConfigurationException("Could not initialize Handlebars due to unrecognized template loader \"" + type + "\"", CONFIG_TEMPLATE_LOADERS);
        }
    }
}
