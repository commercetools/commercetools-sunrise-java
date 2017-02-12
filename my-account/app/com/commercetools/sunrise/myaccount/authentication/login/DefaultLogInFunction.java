package com.commercetools.sunrise.myaccount.authentication.login;

import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.myaccount.authentication.AbstractCustomerInSigner;
import com.commercetools.sunrise.shoppingcart.CartInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerSignInCommand;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultLogInFunction extends AbstractCustomerInSigner implements LogInFunction {

    private final CartInSession cartInSession;

    @Inject
    protected DefaultLogInFunction(final SphereClient sphereClient, final HookContext hookContext, final CartInSession cartInSession) {
        super(sphereClient, hookContext);
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
