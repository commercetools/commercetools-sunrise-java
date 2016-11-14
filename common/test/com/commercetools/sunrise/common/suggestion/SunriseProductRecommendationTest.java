package com.commercetools.sunrise.common.suggestion;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.contexts.UserContextTestProvider;
import com.commercetools.sunrise.common.controllers.TestableSphereClient;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.json.SphereJsonUtils;
import io.sphere.sdk.products.ProductProjection;
import org.junit.Test;

import java.util.Set;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class SunriseProductRecommendationTest {

    @Test
    public void getsNoSuggestionsOnEmptyCategories() throws Exception {
        final Set<ProductProjection> recommendedProducts = productRecommendation(TestableSphereClient.ofEmptyResponse())
                .relatedToCategories(emptyList(), 5).toCompletableFuture().join();
        assertThat(recommendedProducts).isEmpty();
    }

    @Test
    public void getsNoSuggestionsOnEmptyProductCategories() throws Exception {
        final Set<ProductProjection> recommendedProducts = productRecommendation(TestableSphereClient.ofEmptyResponse())
                .relatedToProduct(productWithNoCategories(), 5).toCompletableFuture().join();
        assertThat(recommendedProducts).isEmpty();
    }

    private SunriseProductRecommendation productRecommendation(final SphereClient sphereClient) {
        final Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(SphereClient.class).toInstance(sphereClient);
                bind(UserContext.class).toProvider(UserContextTestProvider.class);
            }
        });
        return injector.getInstance(SunriseProductRecommendation.class);
    }

    private ProductProjection productWithNoCategories() {
        return SphereJsonUtils.readObject("{\"categories\": [], \"variants\": []}", ProductProjection.class);
    }

}
