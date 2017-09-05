package com.commercetools.sunrise.email.fake;

import com.commercetools.sunrise.email.EmailCreationException;
import com.commercetools.sunrise.email.EmailDeliveryException;
import com.commercetools.sunrise.email.EmailSender;
import com.commercetools.sunrise.email.MessageEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Application;

import javax.annotation.Nonnull;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public final class FakeEmailSender implements EmailSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);

    private final File path;

    public FakeEmailSender(final Application application) {
        path = application.path();
    }

    @Nonnull
    @Override
    public CompletionStage<String> send(@Nonnull final MessageEditor messageEditor) {
        final String emailId = UUID.randomUUID().toString();
        final MimeMessage message = createMessage(messageEditor, emailId);
        final CompletableFuture<String> result = new CompletableFuture<>();
        try {
            final File emailFile = createEmailFile(emailId);
            message.writeTo(new FileOutputStream(emailFile));
            LOGGER.debug("Message written in file: " + emailFile.getAbsolutePath());
            result.complete(emailId);
        } catch (Throwable t) {
            result.completeExceptionally(new EmailDeliveryException("Could not write email file", t));
        }
        return result;
    }

    private MimeMessage createMessage(final @Nonnull MessageEditor messageEditor, final String emailId) {
        final Session session = Session.getInstance(new Properties());
        final MimeMessage message = new MimeMessage(session);
        try {
            messageEditor.edit(message);
            LOGGER.debug("Message edited: " + emailId);
        } catch (Exception e) {
            LOGGER.error("Could not send fake email", e);
            throw new EmailCreationException("Could not create fake email", e);
        }
        return message;
    }

    private File createEmailFile(final String emailId) {
        return new File(path, "email-" + emailId + ".eml");
    }
}
