package com.commercetools.sunrise.myaccount.authentication.login;

import com.commercetools.sunrise.sessions.cart.CartInSession;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.myaccount.authentication.AbstractCustomerSignInExecutor;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerSignInCommand;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultLogInExecutor extends AbstractCustomerSignInExecutor implements LogInExecutor {

    private final CartInSession cartInSession;

    @Inject
    protected DefaultLogInExecutor(final SphereClient sphereClient, final HookRunner hookRunner, final CartInSession cartInSession) {
        super(sphereClient, hookRunner);
        this.cartInSession = cartInSession;
    }

    @Override
    public CompletionStage<CustomerSignInResult> apply(final LogInFormData formData) {
        return executeRequest(buildRequest(formData));
    }

    protected CustomerSignInCommand buildRequest(final LogInFormData formData) {
        final String cartId = cartInSession.findCartId().orElse(null);
        return CustomerSignInCommand.of(formData.getUsername(), formData.getPassword(), cartId);
    }
}
