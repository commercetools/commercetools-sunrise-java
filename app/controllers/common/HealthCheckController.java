package controllers.common;

import com.commercetools.sunrise.common.healthcheck.SunriseHealthCheckController;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import io.sphere.sdk.client.SphereClient;

import javax.inject.Inject;

@NoCache
public final class HealthCheckController extends SunriseHealthCheckController {

    @Inject
    public HealthCheckController(final SphereClient sphereClient) {
        super(sphereClient);
    }
}
