package com.commercetools.sunrise.it;

import com.google.inject.AbstractModule;
import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.client.SphereClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import static com.commercetools.sunrise.it.SphereClientFixtures.provideSphereClient;

public abstract class WithSphereClient extends WithApplication {

    protected volatile static BlockingSphereClient sphereClient;

    @BeforeClass
    public synchronized static void startSphereClient() {
        if (sphereClient == null) {
            sphereClient = provideSphereClient();
        }
    }

    @AfterClass
    public synchronized static void stopSphereClient() {
        if (sphereClient != null) {
            sphereClient.close();
            sphereClient = null;
        }
    }

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .overrides(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(SphereClient.class).toInstance(sphereClient);
                    }
                }).build();
    }
}