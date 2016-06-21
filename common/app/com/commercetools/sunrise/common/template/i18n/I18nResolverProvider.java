package com.commercetools.sunrise.common.template.i18n;

import com.commercetools.sunrise.common.SunriseInitializationException;
import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.template.i18n.composite.CompositeI18nResolver;
import com.commercetools.sunrise.common.template.i18n.yaml.YamlI18nResolver;
import com.google.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public final class I18nResolverProvider implements Provider<I18nResolver> {
    private static final Logger logger = LoggerFactory.getLogger(I18nResolverProvider.class);
    private static final String CONFIG_RESOLVER_LOADERS = "application.i18n.resolverLoaders";
    private static final String CONFIG_BUNDLES = "application.i18n.bundles";
    private static final String CONFIG_TYPE_ATTR = "type";
    private static final String CONFIG_PATH_ATTR = "path";
    private static final String YAML_TYPE = "yaml";

    @Inject
    private Configuration configuration;
    @Inject
    private ProjectContext projectContext;

    @Override
    public I18nResolver get() {
        final List<Locale> locales = projectContext.locales();
        final List<String> bundles = getBundles();
        logger.info("Provide CompositeI18nResolver: languages {}, bundles {}", locales, bundles);
        final List<I18nResolver> i18nResolvers = loadI18nResolvers(locales, bundles);
        return CompositeI18nResolver.of(i18nResolvers);
    }

    private List<String> getBundles() {
        final List<String> bundles = configuration.getStringList(CONFIG_BUNDLES, emptyList());
        if (bundles.isEmpty()) {
            throw new SunriseInitializationException("No i18n bundles defined in configuration '" + CONFIG_BUNDLES + "'");
        }
        return bundles;
    }

    private List<I18nResolver> loadI18nResolvers(final List<Locale> locales, final List<String> bundles) {
        final List<Configuration> resolverLoaders = configuration.getConfigList(CONFIG_RESOLVER_LOADERS, emptyList());
        if (resolverLoaders.isEmpty()) {
            throw new SunriseInitializationException("No i18n resolver loaders defined in configuration '" + CONFIG_RESOLVER_LOADERS + "'");
        }
        return resolverLoaders.stream()
                .map(resolverLoader -> loadI18nResolver(resolverLoader, locales, bundles))
                .collect(toList());
    }

    private I18nResolver loadI18nResolver(final Configuration resolverLoader, final List<Locale> locales, final List<String> bundles) {
        final String type = resolverLoader.getString(CONFIG_TYPE_ATTR);
        final String path = resolverLoader.getString(CONFIG_PATH_ATTR);
        if (YAML_TYPE.equals(type)) {
            return YamlI18nResolver.of(path, locales, bundles);
        } else {
            throw new SunriseInitializationException("Not recognized i18n resolver loader: " + type);
        }
    }
}
