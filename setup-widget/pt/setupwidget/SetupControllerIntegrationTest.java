package setupwidget;

import com.commercetools.sunrise.common.WithSunriseApplication;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import play.Application;
import play.Configuration;
import play.Logger;
import play.api.routing.Router;
import play.mvc.Call;
import play.routing.RoutingDsl;
import play.test.TestBrowser;
import setupwidget.controllers.SetupController;
import setupwidget.controllers.SetupReverseRouter;

import javax.inject.Inject;
import java.io.File;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Results.ok;
import static play.test.Helpers.POST;

public class SetupControllerIntegrationTest extends WithSunriseApplication {

    private static final String PREFIX = "SUNRISE_IT_";
    public static final String PROJECT_KEY = PREFIX + "CTP_PROJECT_KEY";
    public static final String CLIENT_SECRET = PREFIX + "CTP_CLIENT_SECRET";
    public static final String CLIENT_ID = PREFIX + "CTP_CLIENT_ID";

    @Ignore // TODO Needs to be properly integrated in the Framework in order to work
    @Test
    public void widget() throws Exception {
        if (isExecutedInCIServer()) {
            run(application(), (browser, port) -> {
                assertThat(devConfFile())
                        .overridingErrorMessage("file dev.conf should be absent")
                        .doesNotExist();
                browser.goTo("http://localhost:" + port);
                assertThat(browser.title()).containsIgnoringCase("credentials setup");
                System.out.println(browser.$("form").getAttribute("action"));
                System.out.println(browser.text("body form"));

                fillAndSubmitSetupForm(browser);
                waitUntilIdVisible(browser, "reload-link");
                assertThat(devConfFile())
                        .overridingErrorMessage("dev.conf should exist.")
                        .exists();
            });
        } else {
            Logger.warn(SetupControllerIntegrationTest.class + " is ignored.");
        }
    }

    private boolean isExecutedInCIServer() {
        return "true".equals(envVar("CI"));
    }

    private void fillAndSubmitSetupForm(final TestBrowser browser) {
        browser.fill("#projectKey").with(envVar(PROJECT_KEY));
        browser.fill("#clientId").with(envVar(CLIENT_ID));
        browser.fill("#clientSecret").with(envVar(CLIENT_SECRET));
        browser.submit("#submit");
    }

    private void waitUntilIdVisible(final TestBrowser browser, final String id) {
        browser.fluentWait()
                .until((WebDriver webDriver) -> webDriver.findElement(By.id(id)).isDisplayed());
    }

    private String envVar(final String project) {
        return System.getenv(project);
    }

    private File devConfFile() {
        return new File("../../conf/dev.conf");
    }

    private Application application() {
        final Module module = new AbstractModule() {
            @Override
            protected void configure() {
                bind(SetupReverseRouter.class).toInstance(() -> new Call() {
                    @Override
                    public String url() {
                        return "/setup";
                    }

                    @Override
                    public String method() {
                        return POST;
                    }

                    @Override
                    public String fragment() {
                        return "";
                    }
                });
                bind(play.api.routing.Router.class).toProvider(new Provider<Router>() {
                    @Inject
                    private SetupController setupController;

                    @Override
                    public Router get() {
                        return new RoutingDsl()
                                .GET("/").routeTo(() -> setupController.handleOrFallback(() -> ok()))
                                .POST("/setup").routeTo(() -> setupController.processForm())
                                .build().asScala();
                    }
                });
            }
        };
        return appBuilder(module)
                .loadConfig(configurationWithSetupEnabled())
                .build();
    }

    private Configuration configurationWithSetupEnabled() {
        final Map<String, Object> configMap = singletonMap(SetupController.CONFIG_SETUP_ENABLED, true);
        return new Configuration(configMap).withFallback(testConfiguration());
    }

}
