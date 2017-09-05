package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.email.EmailSender;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerToken;
import io.sphere.sdk.customers.commands.CustomerCreatePasswordTokenCommand;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultRecoverPasswordControllerAction extends AbstractCustomerCreatePasswordTokenExecutor implements RecoverPasswordControllerAction {

    private final EmailSender emailSender;
    private final RecoverPasswordMessageEditorProvider recoverPasswordMessageEditorProvider;

    @Inject
    protected DefaultRecoverPasswordControllerAction(final SphereClient sphereClient, final HookRunner hookRunner,
                                                     final EmailSender emailSender, final RecoverPasswordMessageEditorProvider recoverPasswordMessageEditorProvider) {
        super(sphereClient, hookRunner);
        this.emailSender = emailSender;
        this.recoverPasswordMessageEditorProvider = recoverPasswordMessageEditorProvider;
    }

    protected final EmailSender getEmailSender() {
        return emailSender;
    }

    protected final RecoverPasswordMessageEditorProvider getRecoverPasswordMessageEditorProvider() {
        return recoverPasswordMessageEditorProvider;
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
        return recoverPasswordMessageEditorProvider.get(resetPasswordToken, recoveryEmailFormData)
                .thenCompose(emailSender::send)
                .thenApply(messageId -> resetPasswordToken);
    }
}
