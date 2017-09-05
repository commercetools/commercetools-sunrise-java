package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import com.commercetools.sunrise.framework.template.engine.EmailContentRenderer;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierResolver;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels.RecoverPasswordEmailContent;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels.RecoverPasswordEmailContentFactory;
import com.commercetools.sunrise.email.MessageEditor;
import io.sphere.sdk.customers.CustomerToken;
import play.libs.concurrent.HttpExecution;
import play.twirl.api.Content;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.mail.Message;
import java.util.concurrent.CompletionStage;

/**
 * Default provider which obtains the email contents from the template {@code password-reset-email}.
 * Subject, from and recipients fields are populated too.
 *
 * If your email requires any additional data you can extend this class, override the method
 * {@link #get(CustomerToken, RecoverPasswordFormData)} and add the necessary
 * lines of code after applying the returned {@link MessageEditor}.
 *
 * For example, the following code uses the editor provided by the parent class, changing the subject and adding some description:
 *
 * <pre>
 * {@code
 * public CompletionStage<MessageEditor> get(final CustomerToken resetPasswordToken, final RecoverPasswordFormData formData) {
 *   return super.get(resetPasswordToken, formData).thenApply(msgEditor -> msg -> {
 *      msgEditor.edit(msg);
 *      msg.setSubject("Another subject");
 *      msg.setDescription("Some description");
 *   });
 * }}
 * </pre>
 *
 * Alternatively you can create your own implementation of {@link RecoverPasswordMessageEditorProvider}.
 */
public class DefaultRecoverPasswordMessageEditorProvider implements RecoverPasswordMessageEditorProvider {

    private final I18nIdentifierResolver i18nIdentifierResolver;
    private final EmailContentRenderer emailContentRenderer;
    private final RecoverPasswordEmailContentFactory recoverPasswordEmailContentFactory;

    @Inject
    protected DefaultRecoverPasswordMessageEditorProvider(final I18nIdentifierResolver i18nIdentifierResolver,
                                                          final EmailContentRenderer emailContentRenderer,
                                                          final RecoverPasswordEmailContentFactory recoverPasswordEmailContentFactory) {
        this.i18nIdentifierResolver = i18nIdentifierResolver;
        this.emailContentRenderer = emailContentRenderer;
        this.recoverPasswordEmailContentFactory = recoverPasswordEmailContentFactory;
    }

    protected final I18nIdentifierResolver getI18nIdentifierResolver() {
        return i18nIdentifierResolver;
    }

    protected final EmailContentRenderer getEmailContentRenderer() {
        return emailContentRenderer;
    }

    protected final RecoverPasswordEmailContentFactory getRecoverPasswordEmailContentFactory() {
        return recoverPasswordEmailContentFactory;
    }

    @Override
    public CompletionStage<MessageEditor> get(final CustomerToken resetPasswordToken, final RecoverPasswordFormData formData) {
        return createEmailContent(resetPasswordToken)
                .thenApplyAsync(emailContent -> msg -> {
                    msg.setContent(emailContent, "text/html");
                    msg.setRecipients(Message.RecipientType.TO, formData.email());
                    msg.setFrom(createFromField());
                    msg.setSubject(createSubjectField(), "UTF-8");
                }, HttpExecution.defaultContext());
    }

    private CompletionStage<String> createEmailContent(final CustomerToken resetPasswordToken) {
        final RecoverPasswordEmailContent viewModel = recoverPasswordEmailContentFactory.create(resetPasswordToken);
        return emailContentRenderer.render(viewModel, "password-reset-email")
                .thenApply(Content::body);
    }

    @Nullable
    private String createFromField() {
        return i18nIdentifierResolver.resolve("my-account:forgotPassword.email.from").orElse(null);
    }

    @Nullable
    private String createSubjectField() {
        return i18nIdentifierResolver.resolve("my-account:forgotPassword.email.subject").orElse(null);
    }
}
