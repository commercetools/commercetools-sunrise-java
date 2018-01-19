package com.commercetools.sunrise.core.hooks.ctpactions;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.expansion.ExpansionPathContainer;

import java.util.concurrent.CompletionStage;

public interface CustomerUpdatedActionHook extends CtpActionHook {

    CompletionStage<Customer> onCustomerUpdatedAction(final Customer customer, final ExpansionPathContainer<Customer> expansionPathContainer);

    static CompletionStage<Customer> runHook(final HookRunner hookRunner, final Customer customer, final ExpansionPathContainer<Customer> expansionPathContainer) {
        return hookRunner.run(CustomerUpdatedActionHook.class, customer, (hook, updatedCart) -> hook.onCustomerUpdatedAction(updatedCart, expansionPathContainer));
    }

}
