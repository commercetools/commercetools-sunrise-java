package demo.common;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.controllers.SunriseHealthCheckController;
import io.sphere.sdk.client.SphereClient;

import javax.inject.Inject;

@NoCache
public final class HealthCheckController extends SunriseHealthCheckController {

    @Inject
    public HealthCheckController(final SphereClient sphereClient) {
        super(sphereClient);
    }
}
