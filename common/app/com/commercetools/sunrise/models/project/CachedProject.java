package com.commercetools.sunrise.models.project;

import com.commercetools.sunrise.core.sessions.ResourceInCache;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.client.SphereTimeoutException;
import io.sphere.sdk.projects.Project;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.client.SphereClientUtils.blockingWait;

@ImplementedBy(DefaultCachedProject.class)
public interface CachedProject extends ResourceInCache<Project> {

    @Override
    CompletionStage<Project> get();

    default Project blockingGet() throws SphereTimeoutException {
        return blockingWait(get(), Duration.ofSeconds(30));
    }
}
