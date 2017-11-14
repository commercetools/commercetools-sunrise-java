package com.commercetools.sunrise.framework.controllers;

import org.junit.Test;
import play.mvc.Http;
import play.test.WithApplication;

import static com.commercetools.sunrise.framework.viewmodels.content.messages.MessageType.INFO;
import static com.commercetools.sunrise.framework.viewmodels.content.messages.MessageType.SUCCESS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.invokeWithContext;

public class SunriseControllerTest extends WithApplication {

    private SunriseController testController = new SunriseController() {};

    @Test
    public void savesMessageInFlash() throws Exception {
        final String message = "Great!";
        invokeWithContext(fakeRequest(), () -> {
            assertThat(Http.Context.current().flash()).isEmpty();
            testController.saveMessage(SUCCESS, message);
            assertThat(Http.Context.current().flash()).containsEntry(SUCCESS.name(), message);
            return null;
        });
    }

    @Test
    public void savesLastMessageOfSameTypeInFlash() throws Exception {
        final String message1 = "foo";
        final String message2 = "bar";
        invokeWithContext(fakeRequest(), () -> {
            assertThat(Http.Context.current().flash()).isEmpty();
            testController.saveMessage(SUCCESS, message1);
            testController.saveMessage(SUCCESS, message2);
            assertThat(Http.Context.current().flash())
                    .doesNotContainEntry(SUCCESS.name(), message1)
                    .containsEntry(SUCCESS.name(), message2);
            return null;
        });
    }

    @Test
    public void savesMessagesOfDifferentTypesInFlash() throws Exception {
        final String message1 = "foo";
        final String message2 = "bar";
        invokeWithContext(fakeRequest(), () -> {
            assertThat(Http.Context.current().flash()).isEmpty();
            testController.saveMessage(SUCCESS, message1);
            testController.saveMessage(INFO, message2);
            assertThat(Http.Context.current().flash())
                    .containsEntry(SUCCESS.name(), message1)
                    .containsEntry(INFO.name(), message2);
            return null;
        });
    }
}
