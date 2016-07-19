package com.commercetools.sunrise.common.ctp;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.sphere.sdk.client.SphereAccessTokenSupplier;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientConfig;
import io.sphere.sdk.client.SphereClientFactory;
import io.sphere.sdk.http.HttpClient;

import javax.inject.Singleton;

/**
 * Configuration for the Guice {@link com.google.inject.Injector} which shall be used in production.
 */
public class CtpModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HttpClient.class).toInstance(SphereClientFactory.of().createHttpClient());
        bind(SphereClientConfig.class).toProvider(SphereClientConfigProvider.class).in(Singleton.class);
        bind(SphereAccessTokenSupplier.class).toProvider(SphereAccessTokenSupplierProvider.class).in(Singleton.class);
        bind(SphereClient.class).annotatedWith(Names.named("global")).toProvider(SphereClientProvider.class).in(Singleton.class);
        bind(SphereClient.class).toProvider(RequestScopedSphereClientProvider.class).in(RequestScoped.class);
        bind(ProductDataConfig.class).toProvider(ProductDataConfigProvider.class).in(Singleton.class);
    }
}
