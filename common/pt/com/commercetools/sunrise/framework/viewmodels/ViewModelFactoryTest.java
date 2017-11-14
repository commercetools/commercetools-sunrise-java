package com.commercetools.sunrise.framework.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.messages.MessageViewModel;
import org.junit.Test;
import play.mvc.Http;
import play.test.WithApplication;

import static com.commercetools.sunrise.framework.viewmodels.ViewModelFactory.extractMessages;
import static com.commercetools.sunrise.framework.viewmodels.content.messages.MessageType.INFO;
import static com.commercetools.sunrise.framework.viewmodels.content.messages.MessageType.SUCCESS;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.invokeWithContext;

public class ViewModelFactoryTest extends WithApplication {

    @Test
    public void extractsSimpleMessage() throws Exception {
        final String message = "foo";
        invokeWithContext(fakeRequest(), () -> {
            Http.Context.current().flash().put(SUCCESS.name(), message);
            assertThat(extractMessages()).containsExactly(messageWithText("success", message));
            return null;
        });
    }

    @Test
    public void extractsMultipleMessages() throws Exception {
        final String message = "foo";
        invokeWithContext(fakeRequest(), () -> {
            final Http.Flash flash = Http.Context.current().flash();
            flash.put(SUCCESS.name(), message);
            flash.put(INFO.name(), message);
            assertThat(extractMessages()).containsExactly(
                    messageWithText("success", message),
                    messageWithText("info", message));
            return null;
        });
    }

    @Test
    public void extractsMessagesAndIgnoresNonExistingTypes() throws Exception {
        final String message = "foo";
        invokeWithContext(fakeRequest(), () -> {
            final Http.Flash flash = Http.Context.current().flash();
            flash.put(SUCCESS.name(), message);
            flash.put("non-existing-type", message);
            assertThat(extractMessages()).containsExactly(messageWithText("success", message));
            return null;
        });
    }

    @Test
    public void extractsEmptyListIfNoMessagesSaved() throws Exception {
        invokeWithContext(fakeRequest(), () -> {
            assertThat(extractMessages()).isEmpty();
            return null;
        });
    }

    private static MessageViewModel messageWithText(final String type, final String message) {
        final MessageViewModel messageViewModel = new MessageViewModel();
        messageViewModel.setType(type);
        messageViewModel.setMessage(message);
        return messageViewModel;
    }
}
