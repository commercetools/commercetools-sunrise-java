package com.commercetools.sunrise.framework.template.i18n.composite;

import com.commercetools.sunrise.framework.template.i18n.I18nResolver;
import com.commercetools.sunrise.framework.template.i18n.I18nResolverInitializationException;
import com.commercetools.sunrise.framework.template.i18n.yaml.YamlI18nResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import java.util.List;
import java.util.Locale;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class CompositeI18nResolverFactory {

    private static final Logger logger = LoggerFactory.getLogger(CompositeI18nResolverFactory.class);
    private static final String CONFIG_RESOLVER_LOADERS = "resolverLoaders";
    private static final String CONFIG_BUNDLES = "bundles";
    private static final String CONFIG_TYPE_ATTR = "type";
    private static final String CONFIG_PATH_ATTR = "path";
    private static final String YAML_TYPE = "yaml";

    public CompositeI18nResolver create(final Configuration configuration, final List<Locale> locales) {
        final List<String> bundles = getBundles(configuration);
        logger.info("Provide CompositeI18nResolver: languages {}, bundles {}", locales, bundles);
        final List<I18nResolver> i18nResolvers = loadI18nResolvers(configuration, locales, bundles);
        return CompositeI18nResolver.of(i18nResolvers);
    }

    protected List<String> getBundles(final Configuration configuration) {
        final List<String> bundles = configuration.getStringList(CONFIG_BUNDLES, emptyList());
        if (bundles.isEmpty()) {
            throw new I18nResolverInitializationException("No i18n bundles defined in configuration '" + CONFIG_BUNDLES + "'");
        }
        return bundles;
    }

    protected List<I18nResolver> loadI18nResolvers(final Configuration configuration, final List<Locale> locales, final List<String> bundles) {
        final List<Configuration> resolverLoaders = configuration.getConfigList(CONFIG_RESOLVER_LOADERS, emptyList());
        if (resolverLoaders.isEmpty()) {
            throw new I18nResolverInitializationException("No i18n resolver loaders defined in configuration '" + CONFIG_RESOLVER_LOADERS + "'");
        }
        return resolverLoaders.stream()
                .map(resolverLoader -> loadI18nResolver(resolverLoader, locales, bundles))
                .collect(toList());
    }

    protected I18nResolver loadI18nResolver(final Configuration resolverLoaderConfiguration, final List<Locale> locales, final List<String> bundles) {
        final String type = resolverLoaderConfiguration.getString(CONFIG_TYPE_ATTR);
        final String path = resolverLoaderConfiguration.getString(CONFIG_PATH_ATTR);
        if (YAML_TYPE.equals(type)) {
            return YamlI18nResolver.of(path, locales, bundles);
        } else {
            throw new I18nResolverInitializationException("Not recognized i18n resolver loader: " + type);
        }
    }
}
