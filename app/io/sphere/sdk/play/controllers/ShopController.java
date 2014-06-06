package io.sphere.sdk.play.controllers;

import io.sphere.sdk.client.PlayJavaClient;
import play.mvc.Controller;

public abstract class ShopController extends Controller {
    private final PlayJavaClient client;

    protected ShopController(PlayJavaClient client) {
        this.client = client;
    }

    protected final PlayJavaClient client() {
        return client;
    }
}
