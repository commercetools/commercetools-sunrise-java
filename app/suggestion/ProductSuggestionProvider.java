package suggestion;

import com.google.inject.Provider;
import io.sphere.sdk.client.SphereClient;
import play.Logger;
import common.suggestion.ProductSuggestion;
import common.suggestion.SunriseProductSuggestion;

import javax.inject.Inject;

class ProductSuggestionProvider implements Provider<ProductSuggestion> {
    private final SphereClient sphere;

    @Inject
    public ProductSuggestionProvider(final SphereClient sphere) {
        this.sphere = sphere;
    }

    @Override
    public ProductSuggestion get() {
        Logger.debug("Provide SunriseProductSuggestion");
        return new SunriseProductSuggestion(sphere);
    }
}
