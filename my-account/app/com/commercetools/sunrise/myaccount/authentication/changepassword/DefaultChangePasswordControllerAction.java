package com.commercetools.sunrise.myaccount.authentication.changepassword;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.MyCustomerInCache;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerChangePasswordCommand;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class DefaultChangePasswordControllerAction extends AbstractCustomerChangePasswordExecutor implements ChangePasswordControllerAction {

    private final MyCustomerInCache myCustomerInCache;

    @Inject
    protected DefaultChangePasswordControllerAction(final SphereClient sphereClient, final HookRunner hookRunner,
                                                    final MyCustomerInCache myCustomerInCache) {
        super(sphereClient, hookRunner);
        this.myCustomerInCache = myCustomerInCache;
    }

    @Override
    public CompletionStage<Customer> apply(final ChangePasswordFormData formData) {
        return myCustomerInCache.require()
                .thenComposeAsync(customer -> executeRequest(buildRequest(formData, customer)), HttpExecution.defaultContext());
    }

    private CustomerChangePasswordCommand buildRequest(final ChangePasswordFormData formData, final Customer customer) {
        return CustomerChangePasswordCommand.of(customer, formData.oldPassword(), formData.newPassword());
    }
}
