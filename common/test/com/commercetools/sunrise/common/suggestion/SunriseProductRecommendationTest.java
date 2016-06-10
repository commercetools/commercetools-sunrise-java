package com.commercetools.sunrise.common.suggestion;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.TestableSphereClient;
import com.neovisionaries.i18n.CountryCode;
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
