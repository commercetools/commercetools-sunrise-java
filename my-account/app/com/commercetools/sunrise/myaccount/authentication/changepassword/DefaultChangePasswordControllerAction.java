package com.commercetools.sunrise.myaccount.authentication.changepassword;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpevents.CustomerChangedPasswordHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.CustomerChangePasswordCommandHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerChangePasswordCommand;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultChangePasswordControllerAction extends AbstractSphereRequestExecutor implements ChangePasswordControllerAction {

    @Inject
    protected DefaultChangePasswordControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Customer> apply(final Customer customer, final ChangePasswordFormData formData) {
        return executeRequest(
                CustomerChangePasswordCommand.of(customer, formData.oldPassword(), formData.newPassword()));
    }

    private CompletionStage<Customer> executeRequest(final CustomerChangePasswordCommand baseCommand) {
        final CustomerChangePasswordCommand command = CustomerChangePasswordCommandHook.runHook(getHookRunner(), baseCommand);
        return getSphereClient().execute(command)
                .thenApplyAsync(updatedCustomer -> {
                    CustomerChangedPasswordHook.runHook(getHookRunner(), updatedCustomer);
                    return updatedCustomer;
                }, HttpExecution.defaultContext());
    }
}
