package common.suggestion;

import com.neovisionaries.i18n.CountryCode;
import com.commercetools.sunrise.common.contexts.UserContext;
import common.controllers.TestableSphereClient;
import com.commercetools.sunrise.common.suggestion.ProductRecommendation;
import com.commercetools.sunrise.common.suggestion.SunriseProductRecommendation;
import org.junit.Test;

import java.util.Locale;

import static io.sphere.sdk.models.DefaultCurrencyUnits.EUR;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class SunriseProductRecommendationTest {

    @Test
    public void getsNoSuggestionsOnEmptyCategories() throws Exception {
        final ProductRecommendation productRecommendation = new SunriseProductRecommendation(TestableSphereClient.ofEmptyResponse());
        assertThat(productRecommendation.relatedToCategories(emptyList(), 5, UserContext.of(singletonList(Locale.GERMAN), CountryCode.DE, EUR)).toCompletableFuture().join()).isEmpty();
    }
}
