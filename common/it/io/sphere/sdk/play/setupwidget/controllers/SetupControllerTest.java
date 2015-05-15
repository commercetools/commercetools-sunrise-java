package io.sphere.sdk.play.setupwidget.controllers;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import play.*;
import play.test.WithBrowser;
import controllers.EnvironmentKeys;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;


public class SetupControllerTest extends WithBrowser {
    @Test
    public void widget() throws Exception {
        if ("true".equals(System.getenv("CI"))) {
            assertThat(devConf().exists()).overridingErrorMessage("file dev.conf should be absent").isFalse();
            browser.goTo("http://localhost:" + port);
            assertThat(browser.title()).containsIgnoringCase("credentials setup");
            browser.fill("#projectKey").with(System.getenv(EnvironmentKeys.PROJECT));
            browser.fill("#clientId").with(System.getenv(EnvironmentKeys.CLIENT_ID));
            browser.fill("#clientSecret").with(System.getenv(EnvironmentKeys.CLIENT_SECRET));
            browser.submit("#submit");
            waitUntilIdVisible("reload-link");
            assertThat(devConf().exists()).overridingErrorMessage("dev.conf should exist.").isTrue();
        } else {
            Logger.warn("" + SetupControllerTest.class + " is ignored.");
        }
    }

    private File devConf() {
        return new File("conf/dev.conf");
    }

    private void waitUntilIdVisible(final String id) {
        browser.fluentWait().until((WebDriver webDriver) ->
            webDriver.findElement(By.id(id)).isDisplayed()
        );
    }
}