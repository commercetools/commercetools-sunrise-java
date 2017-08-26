package com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerPasswordResetCommand;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultResetPasswordControllerAction extends AbstractCustomerPasswordResetExecutor implements ResetPasswordControllerAction {

    @Inject
    public DefaultResetPasswordControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Customer> apply(final String resetToken, final ResetPasswordFormData resetPasswordFormData) {
        return executeRequest(buildRequest(resetToken, resetPasswordFormData));
    }

    protected CustomerPasswordResetCommand buildRequest(final String resetToken, final ResetPasswordFormData formData) {
        return CustomerPasswordResetCommand.ofTokenAndPassword(resetToken, formData.newPassword());
    }

}
