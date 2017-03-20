package com.commercetools.sunrise.recommendations;

import com.commercetools.sunrise.test.TestableSphereClient;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.PriceSelection;
import org.junit.Test;

import javax.money.Monetary;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultProductRecommenderTest {

    @Test
    public void getsNoSuggestionsOnEmptyCategories() throws Exception {
        final List<ProductProjection> recommendedProducts = productRecommendation(TestableSphereClient.ofEmptyResponse())
                .relatedToCategories(emptyList(), 5).toCompletableFuture().join();
        assertThat(recommendedProducts).isEmpty();
    }

    @Test
    public void getsNoSuggestionsOnEmptyProductCategories() throws Exception {
        final ProductProjection productWithNoCategories = mock(ProductProjection.class);
        when(productWithNoCategories.getCategories()).thenReturn(emptySet());
        final List<ProductProjection> recommendedProducts = productRecommendation(TestableSphereClient.ofEmptyResponse())
                .relatedToProduct(productWithNoCategories, 5).toCompletableFuture().join();
        assertThat(recommendedProducts).isEmpty();
    }

    private DefaultProductRecommender productRecommendation(final SphereClient sphereClient) {
        final PriceSelection priceSelection = PriceSelection.of(Monetary.getCurrency("EUR"));
        return new DefaultProductRecommender(sphereClient, priceSelection);
    }
}
