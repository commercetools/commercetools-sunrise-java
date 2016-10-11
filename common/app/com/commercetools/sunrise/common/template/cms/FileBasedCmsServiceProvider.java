package com.commercetools.sunrise.common.template.cms;

import com.commercetools.sunrise.cms.CmsService;
import com.commercetools.sunrise.common.SunriseConfigurationException;
import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.template.cms.filebased.FileBasedCmsService;
import com.commercetools.sunrise.common.template.i18n.composite.CompositeI18nResolver;
import com.commercetools.sunrise.common.template.i18n.composite.CompositeI18nResolverFactory;
import com.google.inject.Provider;
import play.Configuration;

import javax.inject.Inject;
import java.util.Optional;

public final class FileBasedCmsServiceProvider implements Provider<CmsService> {

    private static final String CONFIG_CMS_I18N = "application.cms.i18n";

    @Inject
    private Configuration configuration;
    @Inject
    private ProjectContext projectContext;
    @Inject
    private CompositeI18nResolverFactory compositeI18nResolverFactory;

    @Override
    public CmsService get() {
        return Optional.ofNullable(configuration.getConfig(CONFIG_CMS_I18N))
                .map(this::createFileBasedCmsService)
                .orElseThrow(() -> new SunriseConfigurationException("Could not create FileBasedCmsService, as it requires its own i18nResolver configuration and it is missing", CONFIG_CMS_I18N));
    }

    private FileBasedCmsService createFileBasedCmsService(final Configuration config) {
        final CompositeI18nResolver i18nResolver = compositeI18nResolverFactory.create(config, projectContext.locales());
        return FileBasedCmsService.of(i18nResolver);
    }
}
