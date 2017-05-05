package com.commercetools.sunrise.it;

import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.sphere.sdk.client.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import java.time.Duration;

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

    protected static BlockingSphereClient provideSphereClient() {
        final SphereClient client = SphereClientFactory.of(SphereAsyncHttpClientFactory::create).createClient(sphereClientConfig());
        return BlockingSphereClient.of(client, Duration.ofSeconds(20));
    }

    protected static SphereClientConfig sphereClientConfig() {
        final Config configuration = ConfigFactory.load("it.conf");
        final String projectKey = configuration.getString("ctp.it.projectKey");
        final String clientId = configuration.getString("ctp.it.clientId");
        final String clientSecret = configuration.getString("ctp.it.clientSecret");
        return SphereClientConfig.of(projectKey, clientId, clientSecret);
    }
}