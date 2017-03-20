package com.commercetools.sunrise.it;

import com.google.inject.AbstractModule;
import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.client.SphereAsyncHttpClientFactory;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import java.time.Duration;
import java.util.Optional;

public abstract class WithSphereClient extends WithApplication {

    private static final String IT_PREFIX = "SUNRISE_IT_";
    private static final String IT_CTP_PROJECT_KEY = IT_PREFIX + "CTP_PROJECT_KEY";
    private static final String IT_CTP_CLIENT_SECRET = IT_PREFIX + "CTP_CLIENT_SECRET";
    private static final String IT_CTP_CLIENT_ID = IT_PREFIX + "CTP_CLIENT_ID";

    protected static BlockingSphereClient sphereClient;

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
        final SphereClient client = SphereClientFactory.of(SphereAsyncHttpClientFactory::create)
                .createClient(projectKey(), clientId(), clientSecret());
        return BlockingSphereClient.of(client, Duration.ofSeconds(20));
    }

    @BeforeClass
    public static void startSphereClient() {
        sphereClient = provideSphereClient();
    }

    @AfterClass
    public static void stopSphereClient() {
        if (sphereClient != null) {
            sphereClient.close();
            sphereClient = null;
        }
    }

    protected static String projectKey() {
        return getValueForEnvVar(IT_CTP_PROJECT_KEY);
    }

    protected static String clientId() {
        return getValueForEnvVar(IT_CTP_CLIENT_ID);
    }

    protected static String clientSecret() {
        return getValueForEnvVar(IT_CTP_CLIENT_SECRET);
    }

    protected static String getValueForEnvVar(final String key) {
        return Optional.ofNullable(System.getenv(key))
                .orElseThrow(() -> new RuntimeException(
                        "Missing environment variable " + key + ", please provide the following environment variables for the integration test:\n" +
                                "export " + IT_CTP_PROJECT_KEY + "=\"Your SPHERE.IO project key\"\n" +
                                "export " + IT_CTP_CLIENT_ID + "=\"Your SPHERE.IO client ID\"\n" +
                                "export " + IT_CTP_CLIENT_SECRET + "=\"Your SPHERE.IO client secret\"\n"));
    }
}