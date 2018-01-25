package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.AbstractUserResourceUpdater;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public final class DefaultMyCustomerUpdater extends AbstractUserResourceUpdater<Customer, CustomerUpdateCommand> implements MyCustomerUpdater {

    @Inject
    DefaultMyCustomerUpdater(final HookRunner hookRunner, final SphereClient sphereClient, final MyCustomer myCustomer) {
        super(hookRunner, sphereClient, myCustomer);
    }

    @Override
    protected CustomerUpdateCommand buildRequest(final Customer resource, final List<? extends UpdateAction<Customer>> updateActions) {
        return CustomerUpdateCommand.of(resource, updateActions);
    }

    @Override
    protected CompletionStage<Customer> runHook(final CustomerUpdateCommand request, final Function<CustomerUpdateCommand, CompletionStage<Customer>> execution) {
        return hookRunner().run(MyCustomerUpdaterHook.class, request, execution, h -> h::on);
    }
}
