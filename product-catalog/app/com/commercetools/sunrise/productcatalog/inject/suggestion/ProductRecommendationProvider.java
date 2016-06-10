package com.commercetools.sunrise.productcatalog.inject.suggestion;

import com.google.inject.Provider;
import io.sphere.sdk.client.SphereClient;
import play.Logger;
import com.commercetools.sunrise.common.suggestion.ProductRecommendation;
import com.commercetools.sunrise.common.suggestion.SunriseProductRecommendation;

import javax.inject.Inject;

class ProductRecommendationProvider implements Provider<ProductRecommendation> {

    private final SphereClient sphere;

    @Inject
    public ProductRecommendationProvider(final SphereClient sphere) {
        this.sphere = sphere;
    }

    @Override
    public ProductRecommendation get() {
        Logger.debug("Provide SunriseProductRecommendation");
        return new SunriseProductRecommendation(sphere);
    }
}
