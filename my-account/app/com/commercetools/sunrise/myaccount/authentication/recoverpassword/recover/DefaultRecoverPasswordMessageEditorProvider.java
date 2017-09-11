package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.pages.SunrisePageData;
import com.commercetools.sunrise.common.template.engine.TemplateContext;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.email.MessageEditor;
import io.sphere.sdk.customers.CustomerToken;
import play.inject.Injector;
import play.libs.concurrent.HttpExecution;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.mail.Message;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

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

    @Inject
    private UserContext userContext;
    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private RecoverPasswordEmailContentFactory recoverPasswordEmailContentFactory;
    @Inject
    private Injector injector;


    protected final UserContext getUserContext() {
        return userContext;
    }

    protected final I18nResolver getI18nResolver() {
        return i18nResolver;
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
        final SunrisePageData pageData = new SunrisePageData();
        pageData.setContent(viewModel);
        final TemplateContext templateContext = new TemplateContext(pageData, userContext.locales(), null);
        final String content = injector.instanceOf(TemplateEngine.class).render("password-reset-email", templateContext);
        return completedFuture(content);
    }

    @Nullable
    private String createFromField() {
        final I18nIdentifier i18nIdentifier = I18nIdentifier.of("my-account", "forgotPassword.email.from");
        return i18nResolver.get(userContext.locales(), i18nIdentifier).orElse(null);
    }

    @Nullable
    private String createSubjectField() {
        final I18nIdentifier i18nIdentifier = I18nIdentifier.of("my-account", "forgotPassword.email.subject");
        return i18nResolver.get(userContext.locales(), i18nIdentifier).orElse(null);
    }
}
