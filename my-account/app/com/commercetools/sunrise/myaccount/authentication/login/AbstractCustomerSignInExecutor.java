package com.commercetools.sunrise.myaccount.authentication.login;

import com.commercetools.sunrise.core.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpactions.CustomerSignedInActionHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerSignInResultLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CustomerSignInCommandHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerSignInCommand;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

public abstract class AbstractCustomerSignInExecutor extends AbstractSphereRequestExecutor {

    protected AbstractCustomerSignInExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<CustomerSignInResult> executeRequest(final CustomerSignInCommand baseRequest) {
        final CustomerSignInCommand request = CustomerSignInCommandHook.runHook(getHookRunner(), baseRequest);
        return getSphereClient().execute(request)
                .thenComposeAsync(result -> CustomerSignedInActionHook.runHook(getHookRunner(), result, null)
                                .thenApplyAsync(updatedResult -> {
                                    CustomerSignInResultLoadedHook.runHook(getHookRunner(), updatedResult);
                                    return updatedResult;
                                }, HttpExecution.defaultContext()),
                        HttpExecution.defaultContext());
    }
}