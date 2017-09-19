package com.commercetools.sunrise.framework.template.cms;

import com.commercetools.sunrise.cms.CmsService;
import com.commercetools.sunrise.play.configuration.SunriseConfigurationException;
import com.commercetools.sunrise.framework.localization.ProjectContext;
import com.commercetools.sunrise.framework.template.cms.filebased.FileBasedCmsService;
import com.commercetools.sunrise.framework.template.i18n.composite.CompositeI18nResolver;
import com.commercetools.sunrise.framework.template.i18n.composite.CompositeI18nResolverFactory;
import com.google.inject.Provider;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;

public final class FileBasedCmsServiceProvider implements Provider<CmsService> {

    private static final String CONFIG_CMS_I18N = "application.cms.i18n";

    @Nullable
    private final Configuration i18nCmsConfiguration;
    private final ProjectContext projectContext;
    private final CompositeI18nResolverFactory compositeI18nResolverFactory;

    @Inject
    public FileBasedCmsServiceProvider(final Configuration configuration, final ProjectContext projectContext, final CompositeI18nResolverFactory compositeI18nResolverFactory) {
        this.i18nCmsConfiguration = configuration.getConfig(CONFIG_CMS_I18N);
        if (i18nCmsConfiguration == null) {
            throw new SunriseConfigurationException("Could not initialize FileBasedCmsService, as it requires its own i18nResolver configuration and it is missing", CONFIG_CMS_I18N);
        }
        this.projectContext = projectContext;
        this.compositeI18nResolverFactory = compositeI18nResolverFactory;
    }

    @Override
    public CmsService get() {
        final CompositeI18nResolver i18nResolver = compositeI18nResolverFactory.create(i18nCmsConfiguration, projectContext.locales());
        return FileBasedCmsService.of(i18nResolver);
    }
}
