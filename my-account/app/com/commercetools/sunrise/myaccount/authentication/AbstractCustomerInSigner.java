package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.common.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.hooks.events.CustomerSignInResultLoadedHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

public abstract class AbstractCustomerInSigner extends AbstractSphereRequestExecutor {

    protected AbstractCustomerInSigner(final SphereClient sphereClient, final HookContext hookContext) {
        super(sphereClient, hookContext);
    }

    protected final CompletionStage<CustomerSignInResult> executeRequest(final SphereRequest<CustomerSignInResult> request) {
            return getSphereClient().execute(request)
                    .thenApplyAsync(result -> {
                        CustomerSignInResultLoadedHook.runHook(getHookContext(), result);
                        return result;
                    }, HttpExecution.defaultContext());
    }
}
