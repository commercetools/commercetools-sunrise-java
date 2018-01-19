package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.AbstractResourceCreator;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpactions.CustomerSignedInActionHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CartLoadedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerCreatedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CustomerCreateCommandHook;
import com.commercetools.sunrise.models.carts.MyCart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

public abstract class AbstractMyCustomerCreator extends AbstractResourceCreator<CustomerSignInResult, CustomerDraft, CustomerCreateCommand> implements MyCustomerCreator {

    private final MyCustomer myCustomer;
    private final MyCart myCart;

    protected AbstractMyCustomerCreator(final SphereClient sphereClient, final HookRunner hookRunner,
                                        final MyCustomer myCustomer, final MyCart myCart) {
        super(sphereClient, hookRunner);
        this.myCustomer = myCustomer;
        this.myCart = myCart;
    }

    @Override
    protected CustomerCreateCommand buildRequest(final CustomerDraft draft) {
        return CustomerCreateCommand.of(draft);
    }

    @Override
    protected CompletionStage<CustomerSignInResult> executeRequest(final CustomerCreateCommand baseCommand) {
        final CompletionStage<CustomerSignInResult> resourceStage = super.executeRequest(baseCommand);
        resourceStage.thenAcceptAsync(result -> {
            myCustomer.store(result.getCustomer());
            myCart.store(result.getCart());
        }, HttpExecution.defaultContext());
        return resourceStage;
    }

    @Override
    protected final CompletionStage<CustomerCreateCommand> runRequestHook(final CustomerCreateCommand baseCommand) {
        return CustomerCreateCommandHook.runHook(getHookRunner(), baseCommand);
    }

    @Override
    protected final void runCreatedHook(final HookRunner hookRunner, final CustomerSignInResult resource) {
        if (resource.getCart() != null) {
            CartLoadedHook., h -> h.onLoaded(resource.getCart()));
        }
        hookRunner.run(CustomerCreatedHook.class, h -> h.onCreated(resource.getCustomer()));
    }

    @Override
    protected final CompletionStage<CustomerSignInResult> runActionHook(final HookRunner hookRunner, final CustomerSignInResult resource, final ExpansionPathContainer<CustomerSignInResult> expansionPathContainer) {
        return CustomerSignedInActionHook.runHook(hookRunner, resource, expansionPathContainer);
    }
}
