package common.controllers;

import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.client.SphereClientFactory;
import io.sphere.sdk.client.SphereRequest;
import org.junit.AfterClass;
import play.libs.F;

import java.util.Optional;

public abstract class WithPlayJavaSphereClient {
    private static final String IT_SPHERE_PREFIX = "SPHERE_SUNRISE_IT_";
    private static final String IT_SPHERE_PROJECT_KEY = IT_SPHERE_PREFIX + "PROJECT";
    private static final String IT_SPHERE_CLIENT_SECRET = IT_SPHERE_PREFIX + "CLIENT_SECRET";
    private static final String IT_SPHERE_CLIENT_ID = IT_SPHERE_PREFIX + "CLIENT_ID";

    private static volatile PlayJavaSphereClient sphereClient;

    @AfterClass
    public static void stopJavaClient() {
        if (sphereClient != null) {
            sphereClient.close();
            sphereClient = null;
        }
    }

    protected static <T> F.Promise<T> execute(final SphereRequest<T> sphereRequest) {
        return sphereClient().execute(sphereRequest);
    }

    protected static String projectKey() {
        return getValueForEnvVar(IT_SPHERE_PROJECT_KEY);
    }

    protected static String clientId() {
        return getValueForEnvVar(IT_SPHERE_CLIENT_ID);
    }

    protected static String clientSecret() {
        return getValueForEnvVar(IT_SPHERE_CLIENT_SECRET);
    }

    private synchronized static PlayJavaSphereClient sphereClient() {
        if (sphereClient == null) {
            sphereClient = PlayJavaSphereClient.of(SphereClientFactory.of().createClient(projectKey(), clientId(), clientSecret()));
        }
        return sphereClient;
    }

    private static String getValueForEnvVar(final String key) {
        return Optional.ofNullable(System.getenv(key))
                .orElseThrow(() -> new RuntimeException(
                        "Missing environment variable " + key + ", please provide the following environment variables for the integration test:\n" +
                                "export " + IT_SPHERE_PROJECT_KEY + "=\"Your SPHERE.IO project key\"\n" +
                                "export " + IT_SPHERE_CLIENT_ID + "=\"Your SPHERE.IO client ID\"\n" +
                                "export " + IT_SPHERE_CLIENT_SECRET + "=\"Your SPHERE.IO client secret\"\n"));
    }
}