package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.carts.MyCart;
import com.commercetools.sunrise.models.carts.MyCartInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerDraftBuilder;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import org.apache.commons.lang3.RandomStringUtils;

import javax.inject.Inject;

public final class DefaultMyCustomerCreator extends AbstractMyCustomerCreator {

    private final MyCartInSession myCartInSession;

    @Inject
    DefaultMyCustomerCreator(final SphereClient sphereClient, final HookRunner hookRunner,
                             final MyCustomer myCustomer, final MyCart myCart,
                             final MyCartInSession myCartInSession) {
        super(hookRunner, sphereClient, myCustomer, myCart);
        this.myCartInSession = myCartInSession;
    }

    @Override
    protected CustomerCreateCommand buildRequest(final CustomerDraft customerDraft) {
        return CustomerCreateCommand.of(CustomerDraftBuilder.of(customerDraft)
                .customerNumber(generateCustomerNumber())
                .anonymousCartId(myCartInSession.findId().orElse(null))
                .build());
    }

    private String generateCustomerNumber() {
        return RandomStringUtils.randomNumeric(6);
    }
}
