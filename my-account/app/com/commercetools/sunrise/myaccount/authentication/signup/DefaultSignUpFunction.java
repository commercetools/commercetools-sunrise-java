package com.commercetools.sunrise.myaccount.authentication.signup;

import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.myaccount.authentication.AbstractCustomerInSigner;
import com.commercetools.sunrise.shoppingcart.CartInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import org.apache.commons.lang3.RandomStringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultSignUpFunction extends AbstractCustomerInSigner implements SignUpFunction {

    private final CartInSession cartInSession;

    @Inject
    protected DefaultSignUpFunction(final SphereClient sphereClient, final HookContext hookContext, final CartInSession cartInSession) {
        super(sphereClient, hookContext);
        this.cartInSession = cartInSession;
    }

    @Override
    public CompletionStage<CustomerSignInResult> apply(final SignUpFormData formData) {
        return executeRequest(buildRequest(formData));
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
}
