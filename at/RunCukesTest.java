import cucumber.api.junit.Cucumber;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientConfig;
import io.sphere.sdk.client.SphereClientFactory;
import org.fluentlenium.core.FluentAdapter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import play.Application;
import play.Configuration;
import play.Environment;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.Helpers;
import play.test.TestBrowser;
import play.test.TestServer;

import java.util.HashMap;
import java.util.Map;

import static play.inject.Bindings.bind;

@RunWith(Cucumber.class)
public class RunCukesTest {

    private static Application application;
    private static Integer port = play.api.test.Helpers.testServerPort();
    private static TestServer testServer;
    private static TestBrowser testBrowser;

    @BeforeClass
    public static void startPlayApp() throws Exception {
        application = provideApplication();
        testServer = Helpers.testServer(port, application);
        testServer.start();
        testBrowser = new TestBrowser(Helpers.HTMLUNIT, "http://localhost:" + port);
    }

    @AfterClass
    public static void shutdownPlayApp() throws Exception {
        Helpers.stop(testServer);
        testBrowser.quit();
        testBrowser = null;
        application = null;
        port = null;
        testServer = null;
    }

    public static FluentAdapter fluentAdapter() {
        return testBrowser;
    }

    private static Application provideApplication() {
        final Map<String, Object> additionalSettings = new HashMap<>();
        additionalSettings.put("application.settingsWidget.enabled", false);

        return new GuiceApplicationBuilder()
                .overrides(bind(SphereClient.class).toInstance(newSphereClient()))
                .loadConfig(new Configuration(additionalSettings).withFallback(Configuration.load(new Environment(Mode.TEST))))
                .build();
    }

    private static SphereClient newSphereClient() {
        final SphereClientConfig sphereConfig = SphereClientConfig.ofEnvironmentVariables("SPHERE_SUNRISE_IT");
        return SphereClientFactory.of().createClient(sphereConfig);
    }
}