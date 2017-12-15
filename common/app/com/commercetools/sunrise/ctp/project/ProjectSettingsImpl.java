package com.commercetools.sunrise.ctp.project;

import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
class ProjectSettingsImpl implements ProjectSettings {

    private final String cacheKey;
    private final Integer cacheExpiration;

    @Inject
    ProjectSettingsImpl(final Configuration configuration) {
        this(configuration, "sunrise.ctp.project");
    }

    ProjectSettingsImpl(final Configuration globalConfig, final String configPath) {
        final Configuration config = globalConfig.getConfig(configPath);
        this.cacheKey = config.getString("cacheKey");
        this.cacheExpiration = config.getInt("cacheExpiration");
    }

    @Override
    public String cacheKey() {
        return cacheKey;
    }

    @Override
    public Optional<Integer> cacheExpiration() {
        return Optional.ofNullable(cacheExpiration);
    }
}
