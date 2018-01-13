package com.commercetools.sunrise.myaccount.resetpassword;

import com.google.inject.ImplementedBy;
import com.commercetools.sunrise.email.MessageEditor;
import io.sphere.sdk.customers.CustomerToken;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultResetPasswordRequestMessageEditorProvider.class)
@FunctionalInterface
public interface ResetPasswordRequestMessageEditorProvider {

    /**
     * Provides a {@link MessageEditor} that allows to modify a {@link javax.mail.internet.MimeMessage}
     * with the particular information required to badRequest an email for recovering the password with the given {@code passwordToken}.
     *
     * This interface allows to populate an email with information; typically with subject, content or recipients.
     *
     * @param passwordToken the token to reset the password
     * @param formData the form data for recovering the password
     * @return the completion stage of the message editor
     */
    CompletionStage<MessageEditor> get(final CustomerToken passwordToken, final ResetPasswordRequestFormData formData);
}
