package com.commercetools.sunrise.myaccount.resetpassword;

import com.commercetools.sunrise.core.i18n.I18nResolver;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
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
 * {@link #get(CustomerToken, ResetPasswordRequestFormData)} and add the necessary
 * lines of code after applying the returned {@link MessageEditor}.
 *
 * For example, the following code uses the editor provided by the parent class, changing the subject and adding some description:
 *
 * <pre>
 * {@code
 * public CompletionStage<MessageEditor> get(final CustomerToken resetPasswordToken, final ResetPasswordRequestFormData formData) {
 *   return super.get(resetPasswordToken, formData).thenApply(msgEditor -> msg -> {
 *      msgEditor.edit(msg);
 *      msg.setSubject("Another subject");
 *      msg.setDescription("Some description");
 *   });
 * }}
 * </pre>
 *
 * Alternatively you can create your own implementation of {@link ResetPasswordRequestMessageEditorProvider}.
 */
public class DefaultResetPasswordRequestMessageEditorProvider implements ResetPasswordRequestMessageEditorProvider {

    private final I18nResolver i18nResolver;
    private final TemplateEngine templateEngine;

    @Inject
    protected DefaultResetPasswordRequestMessageEditorProvider(final I18nResolver i18nResolver,
                                                               final TemplateEngine templateEngine) {
        this.i18nResolver = i18nResolver;
        this.templateEngine = templateEngine;
    }

    protected final I18nResolver getI18nResolver() {
        return i18nResolver;
    }

    protected final TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    @Override
    public CompletionStage<MessageEditor> get(final CustomerToken passwordToken, final ResetPasswordRequestFormData formData) {
        return createEmailContent(passwordToken)
                .thenApplyAsync(emailContent -> msg -> {
                    msg.setContent(emailContent, "text/html");
                    msg.setRecipients(Message.RecipientType.TO, formData.email());
                    msg.setFrom(createFromField());
                    msg.setSubject(createSubjectField(), "UTF-8");
                }, HttpExecution.defaultContext());
    }

    private CompletionStage<String> createEmailContent(final CustomerToken resetPasswordToken) {
        return templateEngine.render("password-reset-email", PageData.of()).thenApply(Content::body);
    }

    @Nullable
    private String createFromField() {
        return i18nResolver.get("my-account:forgotPassword.email.from").orElse(null);
    }

    @Nullable
    private String createSubjectField() {
        return i18nResolver.get("my-account:forgotPassword.email.subject").orElse(null);
    }
}
