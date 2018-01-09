package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.carts.MyCartInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerDraftBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class DefaultCustomerCreator extends AbstractCustomerCreator {

    private final MyCartInSession myCartInSession;

    @Inject
    protected DefaultCustomerCreator(final SphereClient sphereClient, final HookRunner hookRunner, final MyCartInSession myCartInSession) {
        super(sphereClient, hookRunner);
        this.myCartInSession = myCartInSession;
    }

    protected final MyCartInSession getMyCartInSession() {
        return myCartInSession;
    }

    @Override
    public CompletionStage<CustomerDraft> defaultDraft(final String email, final String password) {
        return completedFuture(CustomerDraftBuilder.of(email, password)
                .customerNumber(generateCustomerNumber())
                .anonymousCartId(myCartInSession.findId().orElse(null))
                .build());
    }

    @Nullable
    protected String generateCustomerNumber() {
        return RandomStringUtils.randomNumeric(6);
    }
}
