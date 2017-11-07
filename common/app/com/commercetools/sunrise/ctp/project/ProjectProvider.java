package com.commercetools.sunrise.ctp.project;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.projects.Project;
import io.sphere.sdk.projects.queries.ProjectGet;

import javax.inject.Inject;
import javax.inject.Provider;
import java.time.Duration;

import static io.sphere.sdk.client.SphereClientUtils.blockingWait;

/**
 * Provides the commercetools {@link Project} associated with the injected {@link SphereClient}.
 */
public final class ProjectProvider implements Provider<Project> {

    private final SphereClient sphereClient;

    @Inject
    ProjectProvider(final SphereClient sphereClient) {
        this.sphereClient = sphereClient;
    }

    @Override
    public Project get() {
        final SphereRequest<Project> request = ProjectGet.of();
        return blockingWait(sphereClient.execute(request), Duration.ofSeconds(30));
    }
}
