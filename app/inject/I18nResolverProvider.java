package inject;

import com.google.inject.Provider;
import common.contexts.ProjectContext;
import common.i18n.CompositeI18nResolver;
import common.i18n.I18nResolver;
import common.i18n.YamlI18nResolver;
import play.Configuration;
import play.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

class I18nResolverProvider implements Provider<I18nResolver> {
    private static final String CONFIG_RESOLVER_LOADERS = "application.i18n.resolverLoaders";
    private static final String CONFIG_BUNDLES = "application.i18n.bundles";
    private static final String CONFIG_TYPE_ATTR = "type";
    private static final String CONFIG_PATH_ATTR = "path";
    private static final String YAML_TYPE = "yaml";
    private final Configuration configuration;
    private final ProjectContext projectContext;

    @Inject
    public I18nResolverProvider(final Configuration configuration, final ProjectContext projectContext) {
        this.configuration = configuration;
        this.projectContext = projectContext;
    }

    @Override
    public I18nResolver get() {
        final List<Configuration> resolverLoaders = configuration.getConfigList(CONFIG_RESOLVER_LOADERS, emptyList());
        final List<Locale> locales = projectContext.languages();
        final List<String> bundles = configuration.getStringList(CONFIG_BUNDLES, emptyList());
        Logger.debug("Provide CompositeI18nResolver: languages {}, bundles {}", locales, bundles);
        final List<I18nResolver> i18nResolvers = loadI18nResolvers(resolverLoaders, locales, bundles);
        return CompositeI18nResolver.of(i18nResolvers);
    }


    public static List<I18nResolver> loadI18nResolvers(final List<Configuration> resolverLoaders, final List<Locale> locales, final List<String> bundles) {
        if (resolverLoaders.isEmpty()) {
            throw new SunriseInitializationException("No i18n resolver loaders defined in 'application.i18n.resolverLoaders'");
        }
        return resolverLoaders.stream()
                .map(resolverLoader -> loadI18nResolver(resolverLoader, locales, bundles))
                .collect(toList());
    }

    private static YamlI18nResolver loadI18nResolver(final Configuration resolverLoader, final List<Locale> locales, final List<String> bundles) {
        final String type = resolverLoader.getString(CONFIG_TYPE_ATTR);
        final String path = resolverLoader.getString(CONFIG_PATH_ATTR);
        if (YAML_TYPE.equals(type)) {
            return YamlI18nResolver.of(path, locales, bundles);
        } else {
            throw new SunriseInitializationException("Not recognized i18n resolver loader: " + type);
        }
    }
}
