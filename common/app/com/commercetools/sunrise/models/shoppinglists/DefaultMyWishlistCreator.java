package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.CustomerInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.shoppinglists.ShoppingListDraft;
import io.sphere.sdk.shoppinglists.ShoppingListDraftBuilder;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class DefaultMyWishlistCreator extends AbstractShoppingListCreator {

    private final CustomerInSession customerInSession;

    @Inject
    protected DefaultMyWishlistCreator(final SphereClient sphereClient, final HookRunner hookRunner, final CustomerInSession customerInSession) {
        super(sphereClient, hookRunner);
        this.customerInSession = customerInSession;
    }

    @Override
    public CompletionStage<ShoppingListDraft> defaultDraft() {
        return completedFuture(ShoppingListDraftBuilder.of(generateName())
                .customer(customerInSession.findId().map(Customer::referenceOfId).orElse(null))
                .build());
    }

    protected LocalizedString generateName() {
        return LocalizedString.ofEnglish("Wishlist");
    }
}
