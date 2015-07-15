package io.sphere.sdk.play.controllers;

import io.sphere.sdk.client.PlayJavaSphereClient;
import play.mvc.Controller;

public abstract class ShopController extends Controller {
    private final PlayJavaSphereClient sphere;

    protected ShopController(PlayJavaSphereClient sphere) {
        this.sphere = sphere;
    }

    protected final PlayJavaSphereClient sphere() {
        return sphere;
    }
}
