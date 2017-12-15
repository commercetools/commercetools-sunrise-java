package com.commercetools.sunrise.ctp.project;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.projects.Project;
import play.cache.CacheApi;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Provides the commercetools {@link Project} associated with the injected {@link SphereClient}.
 */
public final class CachedProjectProvider implements Provider<Project> {

    private final CacheApi cacheApi;
    private final ProjectSettings projectSettings;
    private final ProjectProvider projectProvider;

    @Inject
    CachedProjectProvider(final CacheApi cacheApi, final ProjectSettings projectSettings, final ProjectProvider projectProvider) {
        this.cacheApi = cacheApi;
        this.projectSettings = projectSettings;
        this.projectProvider = projectProvider;
    }

    @Override
    public Project get() {
        return projectSettings.cacheExpiration()
                .map(expiration -> cacheApi.getOrElse(projectSettings.cacheKey(), projectProvider::get, expiration))
                .orElseGet(() -> cacheApi.getOrElse(projectSettings.cacheKey(), projectProvider::get));
    }
}
