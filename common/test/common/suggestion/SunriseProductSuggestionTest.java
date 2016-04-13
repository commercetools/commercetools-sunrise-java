package common.suggestion;

import common.controllers.TestableSphereClient;
import org.junit.Test;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class SunriseProductSuggestionTest {

    @Test
    public void getsNoSuggestionsOnEmptyCategories() throws Exception {
        final ProductSuggestion productSuggestion = new SunriseProductSuggestion(TestableSphereClient.ofEmptyResponse());
        assertThat(productSuggestion.relatedToCategories(emptyList(), 5).toCompletableFuture().join()).isEmpty();
    }
}
