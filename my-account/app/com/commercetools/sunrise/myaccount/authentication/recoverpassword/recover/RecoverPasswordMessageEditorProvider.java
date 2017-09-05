package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import com.google.inject.ImplementedBy;
import com.commercetools.sunrise.email.MessageEditor;
import io.sphere.sdk.customers.CustomerToken;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultRecoverPasswordMessageEditorProvider.class)
@FunctionalInterface
public interface RecoverPasswordMessageEditorProvider {

    /**
     * Provides a {@link MessageEditor} that allows to modify a {@link javax.mail.internet.MimeMessage}
     * with the particular information required to render an email for recovering the password with the given {@code resetPasswordToken}.
     *
     * This interface allows to populate an email with information; typically with subject, content or recipients.
     *
     * @param resetPasswordToken the token to reset the password
     * @param formData the form data for recovering the password
     * @return the completion stage of the message editor
     */
    CompletionStage<MessageEditor> get(final CustomerToken resetPasswordToken, final RecoverPasswordFormData formData);
}
