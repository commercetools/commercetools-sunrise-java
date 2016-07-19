package com.commercetools.sunrise.common.template.engine.handlebars;

import com.commercetools.sunrise.common.SunriseInitializationException;
import com.commercetools.sunrise.common.template.cms.CmsService;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.cache.HighConcurrencyTemplateCache;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import io.sphere.sdk.models.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class HandlebarsFactory extends Base {
    private static final Logger logger = LoggerFactory.getLogger(HandlebarsFactory.class);
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

    private static List<TemplateLoader> initializeTemplateLoaders(final Configuration configuration, final String configKey) {
        return configuration.getConfigList(configKey, emptyList())
                .stream()
                .map(HandlebarsFactory::initializeTemplateLoader)
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

    public Handlebars create() {
        final List<TemplateLoader> templateLoaders = initializeTemplateLoaders(configuration, CONFIG_TEMPLATE_LOADERS);
        return create(templateLoaders, i18NResolver, cmsService);
    }

    //for testing package scope
    static Handlebars create(final List<TemplateLoader> templateLoaders, final I18nResolver i18NResolver, final CmsService cmsService) {
        if (templateLoaders.isEmpty()) {
            throw new SunriseInitializationException("No Handlebars template loaders found in configuration '" + CONFIG_TEMPLATE_LOADERS + "'");
        }
        logger.info("Provide Handlebars: template loaders [{}]]",
                templateLoaders.stream().map(TemplateLoader::getPrefix).collect(joining(", ")));
        final TemplateLoader[] loaders = templateLoaders.toArray(new TemplateLoader[templateLoaders.size()]);
        final Handlebars handlebars = new Handlebars()
                .with(loaders)
                .with(new HighConcurrencyTemplateCache())
                .infiniteLoops(true);
        handlebars.registerHelper("i18n", new HandlebarsI18nHelper(i18NResolver));
        handlebars.registerHelper("cms", new HandlebarsCmsHelper(cmsService));
        handlebars.registerHelper("json", new HandlebarsJsonHelper<>());
        return handlebars;
    }
}
