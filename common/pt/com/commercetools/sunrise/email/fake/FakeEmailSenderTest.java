package com.commercetools.sunrise.email.fake;

import com.commercetools.sunrise.email.EmailSender;
import com.commercetools.sunrise.email.MessageEditor;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.After;
import org.junit.Test;
import play.test.WithApplication;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class FakeEmailSenderTest extends WithApplication {

    private static final String EMAIL_CONTENT = "Some content";

    private MessageEditor fakeMessageEditor = msg -> msg.setContent(EMAIL_CONTENT, "text/html");

    private File email;

    @After
    public void tearDown() throws Exception {
        if (email != null) {
            email.delete();
            email = null;
        }
    }

    @Test
    public void writesEmail() throws Exception {
        sendFakeEmail(fakeMessageEditor);
        assertThat(email).exists();
        assertThat(Files.toString(email, Charsets.UTF_8)).contains(EMAIL_CONTENT);
    }

    private void sendFakeEmail(final MessageEditor messageEditor) throws InterruptedException, java.util.concurrent.ExecutionException {
        final EmailSender emailSender = new FakeEmailSender(app);
        final String emailId = emailSender.send(messageEditor).toCompletableFuture().get();
        email = app.getFile("email-" + emailId + ".eml");
    }
}