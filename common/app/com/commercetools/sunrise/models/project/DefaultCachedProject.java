package com.commercetools.sunrise.models.project;

import com.commercetools.sunrise.core.sessions.AbstractResourceInCache;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.projects.Project;
import io.sphere.sdk.projects.queries.ProjectGet;
import play.cache.CacheApi;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultCachedProject extends AbstractResourceInCache<Project> implements CachedProject {

    private final ProjectSettings projectSettings;
    private final SphereClient sphereClient;

    @Inject
    DefaultCachedProject(final CacheApi cacheApi, final ProjectSettings projectSettings, final SphereClient sphereClient) {
        super(cacheApi);
        this.projectSettings = projectSettings;
        this.sphereClient = sphereClient;
    }

    @Override
    protected CompletionStage<Project> fetchResource() {
        return sphereClient.execute(ProjectGet.of());
    }

    @Override
    protected String cacheKey() {
        return projectSettings.cacheKey();
    }
}
