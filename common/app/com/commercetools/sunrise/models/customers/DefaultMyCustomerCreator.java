package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.carts.MyCart;
import com.commercetools.sunrise.models.carts.MyCartInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerDraftBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

final class DefaultMyCustomerCreator extends AbstractMyCustomerCreator {

    private final MyCartInSession myCartInSession;

    @Inject
    DefaultMyCustomerCreator(final SphereClient sphereClient, final HookRunner hookRunner,
                             final MyCustomer myCustomer, final MyCart myCart,
                             final MyCartInSession myCartInSession) {
        super(sphereClient, hookRunner, myCustomer, myCart);
        this.myCartInSession = myCartInSession;
    }

    @Override
    public CompletionStage<CustomerDraft> defaultDraft(final String email, final String password) {
        return completedFuture(CustomerDraftBuilder.of(email, password)
                .customerNumber(generateCustomerNumber())
                .anonymousCartId(myCartInSession.findId().orElse(null))
                .build());
    }

    private String generateCustomerNumber() {
        return RandomStringUtils.randomNumeric(6);
    }
}
