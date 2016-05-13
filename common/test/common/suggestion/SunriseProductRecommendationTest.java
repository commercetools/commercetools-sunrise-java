package common.suggestion;

import common.controllers.TestableSphereClient;
import org.junit.Test;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class SunriseProductRecommendationTest {

    @Test
    public void getsNoSuggestionsOnEmptyCategories() throws Exception {
        final ProductRecommendation productRecommendation = new SunriseProductRecommendation(TestableSphereClient.ofEmptyResponse());
        assertThat(productRecommendation.relatedToCategories(emptyList(), 5).toCompletableFuture().join()).isEmpty();
    }
}
