package common.controllers;

import io.sphere.sdk.client.*;
import org.junit.AfterClass;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public abstract class WithPlayJavaSphereClient {
    protected static final int ALLOWED_TIMEOUT = 5000;
    private static final String IT_PREFIX = "SUNRISE_IT_";
    private static final String IT_CTP_PROJECT_KEY = IT_PREFIX + "CTP_PROJECT_KEY";
    private static final String IT_CTP_CLIENT_SECRET = IT_PREFIX + "CTP_CLIENT_SECRET";
    private static final String IT_CTP_CLIENT_ID = IT_PREFIX + "CTP_CLIENT_ID";

    private static volatile PlayJavaSphereClient sphereClient;

    @AfterClass
    public static void stopJavaClient() {
        if (sphereClient != null) {
            sphereClient.close();
            sphereClient = null;
        }
    }

    protected static <T> CompletionStage<T> execute(final SphereRequest<T> sphereRequest) {
        return sphereClient().execute(sphereRequest);
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

    private synchronized static PlayJavaSphereClient sphereClient() {
        if (sphereClient == null) {
            final SphereClient client = SphereClientFactory.of(SphereAsyncHttpClientFactory::create)
                    .createClient(projectKey(), clientId(), clientSecret());
            sphereClient = PlayJavaSphereClient.of(client);
        }
        return sphereClient;
    }

    private static String getValueForEnvVar(final String key) {
        return Optional.ofNullable(System.getenv(key))
                .orElseThrow(() -> new RuntimeException(
                        "Missing environment variable " + key + ", please provide the following environment variables for the integration test:\n" +
                                "export " + IT_CTP_PROJECT_KEY + "=\"Your SPHERE.IO project key\"\n" +
                                "export " + IT_CTP_CLIENT_ID + "=\"Your SPHERE.IO client ID\"\n" +
                                "export " + IT_CTP_CLIENT_SECRET + "=\"Your SPHERE.IO client secret\"\n"));
    }
}