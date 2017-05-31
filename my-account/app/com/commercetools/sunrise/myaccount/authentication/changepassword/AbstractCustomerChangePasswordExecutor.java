package com.commercetools.sunrise.myaccount.authentication.changepassword;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpactions.CustomerSignedInActionHook;
import com.commercetools.sunrise.framework.hooks.ctpevents.CustomerChangedPasswordHook;
import com.commercetools.sunrise.framework.hooks.ctpevents.CustomerSignInResultLoadedHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.CustomerChangePasswordCommandHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.CustomerCreateCommandHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.CustomerSignInCommandHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerChangePasswordCommand;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import io.sphere.sdk.customers.commands.CustomerSignInCommand;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import play.libs.concurrent.HttpExecution;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

public abstract class AbstractCustomerChangePasswordExecutor extends AbstractSphereRequestExecutor {

    protected AbstractCustomerChangePasswordExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<Customer> executeRequest(final CustomerChangePasswordCommand baseCommand) {
        final CustomerChangePasswordCommand command = CustomerChangePasswordCommandHook.runHook(getHookRunner(), baseCommand);
        return getSphereClient().execute(command)
                .thenApplyAsync(updatedCustomer -> {
                    CustomerChangedPasswordHook.runHook(getHookRunner(), updatedCustomer);
                    return updatedCustomer;
                }, HttpExecution.defaultContext());
    }
}
