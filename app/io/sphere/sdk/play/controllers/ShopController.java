package io.sphere.sdk.play.controllers;

import io.sphere.sdk.client.PlayJavaSphereClient;
import play.mvc.Controller;

public abstract class ShopController extends Controller {
    private final PlayJavaSphereClient client;

    protected ShopController(PlayJavaSphereClient client) {
        this.client = client;
    }

    protected final PlayJavaSphereClient client() {
        return client;
    }
}
