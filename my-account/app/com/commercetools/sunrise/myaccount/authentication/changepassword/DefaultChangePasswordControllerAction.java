package com.commercetools.sunrise.myaccount.authentication.changepassword;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerChangePasswordCommand;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultChangePasswordControllerAction extends AbstractCustomerChangePasswordExecutor implements ChangePasswordControllerAction {

    @Inject
    protected DefaultChangePasswordControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Customer> apply(final Customer customer, final ChangePasswordFormData formData) {
        return executeRequest(buildRequest(customer, formData));
    }

    protected CustomerChangePasswordCommand buildRequest(final Customer customer, final ChangePasswordFormData formData) {
        return CustomerChangePasswordCommand.of(customer, formData.oldPassword(), formData.newPassword());
    }
}
