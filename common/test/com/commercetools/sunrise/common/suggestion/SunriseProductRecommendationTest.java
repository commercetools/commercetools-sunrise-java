package com.commercetools.sunrise.common.suggestion;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.contexts.UserContextImpl;
import com.commercetools.sunrise.common.controllers.TestableSphereClient;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import org.junit.Test;

import java.util.Locale;
import java.util.Set;

import static io.sphere.sdk.models.DefaultCurrencyUnits.EUR;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class SunriseProductRecommendationTest {

    private static final UserContext USER_CONTEXT = UserContextImpl.of(singletonList(Locale.GERMAN), CountryCode.DE, EUR);

    @Test
    public void getsNoSuggestionsOnEmptyCategories() throws Exception {
        final Set<ProductProjection> recommendedProducts = productRecommendation(TestableSphereClient.ofEmptyResponse())
                .relatedToCategories(emptyList(), 5).toCompletableFuture().join();
        assertThat(recommendedProducts).isEmpty();
    }

    private SunriseProductRecommendation productRecommendation(final SphereClient sphereClient) {
        final Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(SphereClient.class).toInstance(sphereClient);
                bind(UserContext.class).toInstance(USER_CONTEXT);
            }
        });
        return injector.getInstance(SunriseProductRecommendation.class);
    }
}
