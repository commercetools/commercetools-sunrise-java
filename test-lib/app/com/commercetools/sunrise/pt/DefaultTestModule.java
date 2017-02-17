package com.commercetools.sunrise.pt;

import com.commercetools.sunrise.test.TestableSphereClient;
import com.google.inject.AbstractModule;
import io.sphere.sdk.client.SphereClient;
import play.routing.RoutingDsl;

public class DefaultTestModule extends AbstractModule {

    @Override
    protected void configure() {
        // TODO Removes all routes to avoid having to resolve all injections while we have routes, remove later!
        bind(play.api.routing.Router.class).toInstance(new RoutingDsl().build().asScala());
        bind(SphereClient.class).toInstance(TestableSphereClient.ofEmptyResponse());
    }
}
