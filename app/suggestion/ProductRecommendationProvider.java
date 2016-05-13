package suggestion;

import com.google.inject.Provider;
import io.sphere.sdk.client.SphereClient;
import play.Logger;
import common.suggestion.ProductRecommendation;
import common.suggestion.SunriseProductRecommendation;

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
