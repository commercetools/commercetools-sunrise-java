package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.AbstractUserResourceUpdater;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpactions.CustomerUpdatedActionHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerUpdatedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CustomerUpdateCommandHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.expansion.ExpansionPathContainer;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

final class MyCustomerUpdaterImpl extends AbstractUserResourceUpdater<Customer, CustomerUpdateCommand> implements MyCustomerUpdater {

    @Inject
    MyCustomerUpdaterImpl(final SphereClient sphereClient, final HookRunner hookRunner, final MyCustomer myCustomer) {
        super(sphereClient, hookRunner, myCustomer);
    }

    @Override
    protected CustomerUpdateCommand buildUpdateCommand(final List<? extends UpdateAction<Customer>> updateActions, final Customer resource) {
        return CustomerUpdateCommand.of(resource, updateActions);
    }

    @Override
    protected CompletionStage<CustomerUpdateCommand> runRequestHook(final HookRunner hookRunner, final CustomerUpdateCommand baseCommand) {
        return CustomerUpdateCommandHook.runHook(hookRunner, baseCommand);
    }

    @Override
    protected CompletionStage<Customer> runResponseHook(final HookRunner hookRunner, final Customer resource,
                                                        final ExpansionPathContainer<Customer> expansionPathContainer) {
        return CustomerUpdatedActionHook.runHook(hookRunner, resource, expansionPathContainer);
    }

    @Override
    protected void runUpdatedHook(final HookRunner hookRunner, final Customer resource) {
        CustomerUpdatedHook.runHook(hookRunner, resource);
    }
}
