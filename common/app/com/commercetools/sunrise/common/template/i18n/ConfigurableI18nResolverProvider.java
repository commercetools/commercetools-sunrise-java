package com.commercetools.sunrise.common.template.i18n;

import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.template.i18n.composite.CompositeI18nResolverFactory;
import com.google.inject.Provider;
import play.Configuration;

import javax.inject.Inject;

public final class ConfigurableI18nResolverProvider implements Provider<I18nResolver> {

    @Inject
    private Configuration configuration;
    @Inject
    private ProjectContext projectContext;
    @Inject
    private CompositeI18nResolverFactory compositeI18nResolverFactory;

    @Override
    public I18nResolver get() {
        final Configuration config = configuration.getConfig("application.i18n");
        return compositeI18nResolverFactory.create(config, projectContext.locales());
    }
}
