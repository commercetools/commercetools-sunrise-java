package com.commercetools.sunrise.common.suggestion;

import com.commercetools.sunrise.test.TestableSphereClient;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.json.SphereJsonUtils;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.PriceSelection;
import org.junit.Test;

import javax.money.Monetary;
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
        final PriceSelection priceSelection = PriceSelection.of(Monetary.getCurrency("EUR"));
        return new SunriseProductRecommendation(sphereClient, priceSelection);
    }

    private ProductProjection productWithNoCategories() {
        return SphereJsonUtils.readObject("{\"categories\": [], \"variants\": []}", ProductProjection.class);
    }

}
