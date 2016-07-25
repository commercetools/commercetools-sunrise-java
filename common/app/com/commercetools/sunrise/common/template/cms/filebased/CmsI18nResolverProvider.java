package com.commercetools.sunrise.common.template.cms.filebased;

import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.common.template.i18n.composite.CompositeI18nResolverFactory;
import com.google.inject.Provider;
import play.Configuration;

import javax.inject.Inject;

public final class CmsI18nResolverProvider implements Provider<I18nResolver> {

    @Inject
    private Configuration configuration;
    @Inject
    private ProjectContext projectContext;
    @Inject
    private CompositeI18nResolverFactory compositeI18nResolverFactory;

    @Override
    public I18nResolver get() {
        final Configuration config = configuration.getConfig("application.cms.i18n");
        return compositeI18nResolverFactory.create(config, projectContext.locales());
    }
}
