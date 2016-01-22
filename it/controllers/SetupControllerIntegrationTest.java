package controllers;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import play.Logger;
import play.test.WithBrowser;

import java.io.File;

import static controllers.EnvironmentKeys.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SetupControllerIntegrationTest extends WithBrowser {

    @Test
    public void widget() throws Exception {
        if (isExecutedInCIServer()) {
            assertThat(devConfFile().exists()).overridingErrorMessage("file dev.conf should be absent").isFalse();
            browser.goTo("http://localhost:" + port);
            assertThat(browser.title()).containsIgnoringCase("credentials setup");
            fillAndSubmitSetupForm();
            waitUntilIdVisible("reload-link");
            assertThat(devConfFile().exists()).overridingErrorMessage("dev.conf should exist.").isTrue();
        } else {
            Logger.warn(SetupControllerIntegrationTest.class + " is ignored.");
        }
    }

    private boolean isExecutedInCIServer() {
        return "true".equals(envVar("CI"));
    }

    private void fillAndSubmitSetupForm() {
        browser.fill("#projectKey").with(envVar(PROJECT_KEY));
        browser.fill("#clientId").with(envVar(CLIENT_ID));
        browser.fill("#clientSecret").with(envVar(CLIENT_SECRET));
        browser.submit("#submit");
    }

    private void waitUntilIdVisible(final String id) {
        browser.fluentWait().until((WebDriver webDriver) ->
                        webDriver.findElement(By.id(id)).isDisplayed()
        );
    }

    private String envVar(final String project) {
        return System.getenv(project);
    }

    private File devConfFile() {
        return new File("conf/dev.conf");
    }
}
