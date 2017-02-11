package com.commercetools.sunrise.myaccount.authentication.signup;

import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.hooks.events.CustomerSignInResultLoadedHook;
import com.commercetools.sunrise.shoppingcart.CartInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import org.apache.commons.lang3.RandomStringUtils;
import play.libs.concurrent.HttpExecution;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultCustomerCreator implements CustomerCreator {

    private final CartInSession cartInSession;
    private final SphereClient sphereClient;
    private final HookContext hookContext;

    @Inject
    protected DefaultCustomerCreator(final CartInSession cartInSession, final SphereClient sphereClient, final HookContext hookContext) {
        this.cartInSession = cartInSession;
        this.sphereClient = sphereClient;
        this.hookContext = hookContext;
    }

    @Override
    public CompletionStage<CustomerSignInResult> createCustomer(final SignUpFormData formData) {
        final CustomerCreateCommand command = buildRequest(formData);
        return sphereClient.execute(command)
                .thenApplyAsync(customer -> {
                    runHookOnCustomerSignedIn(customer);
                    return customer;
                }, HttpExecution.defaultContext());
    }

    protected CustomerCreateCommand buildRequest(final SignUpFormData formData) {
        return CustomerCreateCommand.of(buildDraft(formData));
    }

    @Nullable
    protected String generateCustomerNumber() {
        return RandomStringUtils.randomNumeric(6);
    }

    private CustomerDraft buildDraft(final SignUpFormData formData) {
        final String cartId = cartInSession.findCartId().orElse(null);
        return formData.toCustomerDraftBuilder()
                .customerNumber(generateCustomerNumber())
                .anonymousCartId(cartId)
                .build();
    }

    private void runHookOnCustomerSignedIn(final CustomerSignInResult customer) {
        CustomerSignInResultLoadedHook.runHook(hookContext, customer);
    }
}
