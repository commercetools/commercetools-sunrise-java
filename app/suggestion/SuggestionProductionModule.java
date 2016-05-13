package suggestion;

import com.google.inject.AbstractModule;
import common.suggestion.ProductRecommendation;

/**
 * Configuration for the Guice {@link com.google.inject.Injector} which shall be used in production.
 */
public class SuggestionProductionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProductRecommendation.class).toProvider(ProductRecommendationProvider.class);
    }
}
