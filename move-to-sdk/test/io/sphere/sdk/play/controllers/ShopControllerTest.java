package io.sphere.sdk.play.controllers;

import io.sphere.sdk.client.PlayJavaSphereClient;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ShopControllerTest {

    @Test
    public void initializes() throws Exception {
        final PlayJavaSphereClient client = PlayJavaSphereClient.of(null);
        final ShopControllerExt controller = new ShopControllerExt(client);
        assertThat(controller.client()).isEqualTo(client);
    }

    static class ShopControllerExt extends ShopController {
        public ShopControllerExt(final PlayJavaSphereClient client) {
            super(client);
        }
    }
}
