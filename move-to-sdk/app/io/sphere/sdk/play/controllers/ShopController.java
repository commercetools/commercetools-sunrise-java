package io.sphere.sdk.play.controllers;

import io.sphere.sdk.client.SphereClient;
import play.mvc.Controller;

public abstract class ShopController extends Controller {
    private final SphereClient sphere;

    protected ShopController(SphereClient sphere) {
        this.sphere = sphere;
    }

    protected final SphereClient sphere() {
        return sphere;
    }
}
