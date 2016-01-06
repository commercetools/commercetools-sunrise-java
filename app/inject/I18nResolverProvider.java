package inject;

import com.google.inject.Provider;
import common.contexts.ProjectContext;
import common.i18n.I18nResolver;
import play.Configuration;
import play.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.emptyList;

class I18nResolverProvider implements Provider<I18nResolver> {
    private static final String CONFIG_FILEPATH = "application.i18n.filepath";
    private static final String CONFIG_BUNDLES = "application.i18n.bundles";
    private final Configuration configuration;
    private final ProjectContext projectContext;

    @Inject
    public I18nResolverProvider(final Configuration configuration, final ProjectContext projectContext) {
        this.configuration = configuration;
        this.projectContext = projectContext;
    }

    @Override
    public I18nResolver get() {
        final String filepath = configuration.getString(CONFIG_FILEPATH);
        final List<Locale> locales = projectContext.languages();
        final List<String> bundles = configuration.getStringList(CONFIG_BUNDLES, emptyList());
        Logger.debug("Provide I18nMessagesService: languages {}, bundles {}", locales, bundles);
        return I18nResolver.of(filepath, locales, bundles);
    }
}
