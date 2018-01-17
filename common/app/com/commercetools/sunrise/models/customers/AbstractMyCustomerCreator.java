package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.AbstractResourceCreator;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpactions.CustomerSignedInActionHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerSignInResultLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CustomerCreateCommandHook;
import com.commercetools.sunrise.models.carts.MyCartInCache;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

public abstract class AbstractMyCustomerCreator extends AbstractResourceCreator<CustomerSignInResult, CustomerDraft, CustomerCreateCommand> implements MyCustomerCreator {

    private final MyCustomerInCache myCustomerInCache;
    private final MyCartInCache myCartInCache;

    protected AbstractMyCustomerCreator(final SphereClient sphereClient, final HookRunner hookRunner,
                                        final MyCustomerInCache myCustomerInCache, final MyCartInCache myCartInCache) {
        super(sphereClient, hookRunner);
        this.myCustomerInCache = myCustomerInCache;
        this.myCartInCache = myCartInCache;
    }

    @Override
    protected CustomerCreateCommand buildRequest(final CustomerDraft draft) {
        return CustomerCreateCommand.of(draft);
    }

    @Override
    protected CompletionStage<CustomerSignInResult> executeRequest(final CustomerCreateCommand baseCommand) {
        final CompletionStage<CustomerSignInResult> resourceStage = super.executeRequest(baseCommand);
        resourceStage.thenAcceptAsync(result -> {
            myCustomerInCache.store(result.getCustomer());
            myCartInCache.store(result.getCart());
        }, HttpExecution.defaultContext());
        return resourceStage;
    }

    @Override
    protected final CompletionStage<CustomerCreateCommand> runRequestHook(final HookRunner hookRunner, final CustomerCreateCommand baseCommand) {
        return CustomerCreateCommandHook.runHook(hookRunner, baseCommand);
    }

    @Override
    protected final void runCreatedHook(final HookRunner hookRunner, final CustomerSignInResult resource) {
        CustomerSignInResultLoadedHook.runHook(hookRunner, resource);
    }

    @Override
    protected final CompletionStage<CustomerSignInResult> runActionHook(final HookRunner hookRunner, final CustomerSignInResult resource, final ExpansionPathContainer<CustomerSignInResult> expansionPathContainer) {
        return CustomerSignedInActionHook.runHook(hookRunner, resource, expansionPathContainer);
    }
}
