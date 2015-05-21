package io.sphere.sdk.play.controllers;

import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.play.metrics.MetricAction;
import play.mvc.Controller;
import play.mvc.With;

@With(MetricAction.class)
public abstract class ShopController extends Controller {
    private final PlayJavaSphereClient client;

    protected ShopController(PlayJavaSphereClient client) {
        this.client = client;
    }

    protected final PlayJavaSphereClient client() {
        return client;
    }
}
