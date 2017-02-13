package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.common.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.hooks.HookRunner;
import com.commercetools.sunrise.hooks.events.CustomerSignInResultLoadedHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

public abstract class AbstractCustomerSignInExecutor extends AbstractSphereRequestExecutor {

    protected AbstractCustomerSignInExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<CustomerSignInResult> executeRequest(final SphereRequest<CustomerSignInResult> request) {
            return getSphereClient().execute(request)
                    .thenApplyAsync(result -> {
                        CustomerSignInResultLoadedHook.runHook(getHookRunner(), result);
                        return result;
                    }, HttpExecution.defaultContext());
    }
}
