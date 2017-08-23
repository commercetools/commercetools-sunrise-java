package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.template.engine.TemplateContext;
import com.commercetools.sunrise.framework.template.engine.TemplateEngine;
import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels.RecoverPasswordEmailPageContent;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels.RecoverPasswordEmailPageContentFactory;
import io.commercetools.sunrise.email.EmailSender;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerToken;
import io.sphere.sdk.customers.commands.CustomerCreatePasswordTokenCommand;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import javax.mail.Message;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.CompletionStage;

public class DefaultRecoverPasswordControllerAction extends AbstractCustomerCreatePasswordTokenExecutor implements RecoverPasswordControllerAction {
    private RecoverPasswordEmailPageContentFactory recoverPasswordEmailPageContentFactory;
    private final TemplateEngine templateEngine;
    private final EmailSender emailSender;
    private final Locale locale;

    @Inject
    protected DefaultRecoverPasswordControllerAction(final SphereClient sphereClient, final HookRunner hookRunner,
                                                     final RecoverPasswordEmailPageContentFactory recoverPasswordEmailPageContentFactory,
                                                     final TemplateEngine templateEngine,
                                                     final EmailSender emailSender,
                                                     final Locale locale) {
        super(sphereClient, hookRunner);
        this.recoverPasswordEmailPageContentFactory = recoverPasswordEmailPageContentFactory;
        this.templateEngine = templateEngine;
        this.emailSender = emailSender;
        this.locale = locale;
    }

    @Override
    public CompletionStage<CustomerToken> apply(final RecoverPasswordFormData recoveryEmailFormData) {
        return executeRequest(buildRequest(recoveryEmailFormData))
                .thenComposeAsync(customerToken -> onResetPasswordTokenCreated(customerToken, recoveryEmailFormData), HttpExecution.defaultContext());
    }

    protected CustomerCreatePasswordTokenCommand buildRequest(final RecoverPasswordFormData formData) {
        return CustomerCreatePasswordTokenCommand.of(formData.email());
    }

    protected CompletionStage<CustomerToken> onResetPasswordTokenCreated(final CustomerToken resetPasswordToken, final RecoverPasswordFormData recoveryEmailFormData) {
        final RecoverPasswordEmailPageContent recoverPasswordEmailPageContent =
                recoverPasswordEmailPageContentFactory.create(resetPasswordToken);
        final PageData pageData = new PageData();
        pageData.setContent(recoverPasswordEmailPageContent);

        final TemplateContext templateContext = new TemplateContext(pageData, Collections.singletonList(locale), null);
        final String emailText = templateEngine.render("password-reset-email", templateContext);

        return emailSender.send(msg -> {
            msg.setRecipients(Message.RecipientType.TO, recoveryEmailFormData.email());
            msg.setSubject(recoverPasswordEmailPageContent.getSubject(), "UTF-8");
            msg.setContent(emailText, "text/html");
        }).thenApply(s -> resetPasswordToken);
    }
}
