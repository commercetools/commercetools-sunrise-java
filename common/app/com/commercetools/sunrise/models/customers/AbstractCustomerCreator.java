package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.controllers.AbstractResourceCreator;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpactions.CustomerSignedInActionHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerSignInResultLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CustomerCreateCommandHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import io.sphere.sdk.expansion.ExpansionPathContainer;

import java.util.concurrent.CompletionStage;

public abstract class AbstractCustomerCreator extends AbstractResourceCreator<CustomerSignInResult, CustomerDraft, CustomerCreateCommand> implements CustomerCreator {

    protected AbstractCustomerCreator(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    protected CustomerCreateCommand buildRequest(final CustomerDraft draft) {
        return CustomerCreateCommand.of(draft);
    }

    @Override
    protected CustomerCreateCommand runCreateCommandHook(final HookRunner hookRunner, final CustomerCreateCommand baseCommand) {
        return CustomerCreateCommandHook.runHook(hookRunner, baseCommand);
    }

    @Override
    protected CompletionStage<?> runCreatedHook(final HookRunner hookRunner, final CustomerSignInResult resource) {
        return CustomerSignInResultLoadedHook.runHook(hookRunner, resource);
    }

    @Override
    protected CompletionStage<CustomerSignInResult> runActionHook(final HookRunner hookRunner, final CustomerSignInResult resource, final ExpansionPathContainer<CustomerSignInResult> expansionPathContainer) {
        return CustomerSignedInActionHook.runHook(hookRunner, resource, expansionPathContainer);
    }
}
