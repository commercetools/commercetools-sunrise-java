package com.commercetools.sunrise.common.configuration;

import play.ApplicationLoader;
import play.Configuration;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;

import static play.inject.Bindings.bind;

public class SunriseApplicationLoader extends GuiceApplicationLoader {

    @Override
    public GuiceApplicationBuilder builder(final ApplicationLoader.Context context) {
        return super.builder(context).overrides(
                bind(Configuration.class).toInstance(new SunriseConfiguration(context.initialConfiguration()))
        );
    }
}
