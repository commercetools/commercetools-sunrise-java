package com.commercetools.sunrise.models.project;

import com.google.inject.ImplementedBy;
import play.Configuration;

import java.util.Optional;

@ImplementedBy(ProjectSettingsImpl.class)
public interface ProjectSettings {

    /**
     * @return key from the cache where to store the project
     */
    String cacheKey();

    /**
     * @return time in seconds until the project in cache expires
     */
    Optional<Integer> cacheExpiration();

    static ProjectSettings of(final Configuration globalConfig, final String configPath) {
        return new ProjectSettingsImpl(globalConfig, configPath);
    }
}