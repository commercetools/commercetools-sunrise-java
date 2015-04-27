package controllers;

import io.sphere.sdk.client.HttpRequestIntent;

import java.util.function.Function;

public class ProductControllerTest extends WithSunriseApplication {

    @Override
    protected Function<HttpRequestIntent, Object> getTestDoubleBehavior() {
        return null;
    }
}
