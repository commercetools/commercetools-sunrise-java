package com.commercetools.sunrise.common.suggestion;

import com.google.inject.AbstractModule;

/**
 * Configuration for the Guice {@link com.google.inject.Injector} which shall be used in production.
 */
public class ProductRecommendationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProductRecommendation.class).to(SunriseProductRecommendation.class);
    }
}
