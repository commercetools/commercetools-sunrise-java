package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.controllers.AbstractResourceCreator;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctprequests.CustomerCreateCommandHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import io.sphere.sdk.customers.commands.CustomerSignInCommand;

import java.util.concurrent.CompletionStage;

public abstract class AbstractCustomerSignIn extends AbstractResourceCreator<CustomerSignInResult, CustomerSignInCommand> implements CustomerCreator {

    protected AbstractCustomerSignIn(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<CustomerSignInResult> apply(final SignUpFormData formData) {
        return executeRequest(buildRequest(formData));
    }

    protected final CompletionStage<CustomerSignInResult> executeRequest(final CustomerCreateCommand baseRequest) {
        final CustomerCreateCommand request = CustomerCreateCommandHook.runHook(getHookRunner(), baseRequest);
        return executeRequest(request, request);
    }

    protected abstract CustomerCreateCommand buildRequest(SignUpFormData formData);

    // TODO Add action hook
}
