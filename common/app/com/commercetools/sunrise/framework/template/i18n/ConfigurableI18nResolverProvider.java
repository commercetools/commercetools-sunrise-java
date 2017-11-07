package com.commercetools.sunrise.framework.template.i18n;

import com.commercetools.sunrise.play.configuration.SunriseConfigurationException;
import com.commercetools.sunrise.ctp.project.ProjectContext;
import com.commercetools.sunrise.framework.template.i18n.composite.CompositeI18nResolverFactory;
import com.google.inject.Provider;
import play.Configuration;

import javax.inject.Inject;

public final class ConfigurableI18nResolverProvider implements Provider<I18nResolver> {

    private static final String CONFIG_I18N = "application.i18n";

    private final Configuration i18nConfiguration;
    private final ProjectContext projectContext;
    private final CompositeI18nResolverFactory compositeI18nResolverFactory;

    @Inject
    public ConfigurableI18nResolverProvider(final Configuration configuration, final ProjectContext projectContext,
                                            final CompositeI18nResolverFactory compositeI18nResolverFactory) {
        this.i18nConfiguration = configuration.getConfig(CONFIG_I18N);
        if (i18nConfiguration == null) {
            throw new SunriseConfigurationException("Could not initialize I18nResolver due to missing configuration", CONFIG_I18N);
        }
        this.projectContext = projectContext;
        this.compositeI18nResolverFactory = compositeI18nResolverFactory;
    }

    @Override
    public I18nResolver get() {
        return compositeI18nResolverFactory.create(i18nConfiguration, projectContext.locales());
    }
}
