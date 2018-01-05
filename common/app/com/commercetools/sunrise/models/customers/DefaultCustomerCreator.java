package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.carts.CartInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerDraftBuilder;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import org.apache.commons.lang3.RandomStringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class DefaultCustomerCreator extends AbstractCustomerCreator {

    private final CartInSession cartInSession;

    @Inject
    protected DefaultCustomerCreator(final SphereClient sphereClient, final HookRunner hookRunner, final CartInSession cartInSession) {
        super(sphereClient, hookRunner);
        this.cartInSession = cartInSession;
    }

    @Override
    protected CustomerCreateCommand buildRequest(final SignUpFormData formData) {
        return CustomerCreateCommand.of(buildDraft(formData));
    }

    private CustomerDraft buildDraft(final SignUpFormData formData) {
        final String cartId = cartInSession.findId().orElse(null);
        return CustomerDraftBuilder.of(formData.customerDraft())
                .customerNumber(generateCustomerNumber())
                .anonymousCartId(cartId)
                .build();
    }

    @Nullable
    protected String generateCustomerNumber() {
        return RandomStringUtils.randomNumeric(6);
    }
}
