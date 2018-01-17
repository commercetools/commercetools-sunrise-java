package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.MyCustomerInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.shoppinglists.ShoppingListDraft;
import io.sphere.sdk.shoppinglists.ShoppingListDraftBuilder;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

final class DefaultMyWishlistCreator extends AbstractMyWishlistCreator {

    private final MyCustomerInSession myCustomerInSession;

    @Inject
    DefaultMyWishlistCreator(final SphereClient sphereClient, final HookRunner hookRunner,
                             final MyWishlistInCache myWishlistInCache, final MyCustomerInSession myCustomerInSession) {
        super(sphereClient, hookRunner, myWishlistInCache);
        this.myCustomerInSession = myCustomerInSession;
    }

    @Override
    public CompletionStage<ShoppingListDraft> defaultDraft() {
        return completedFuture(ShoppingListDraftBuilder.of(generateName())
                .customer(myCustomerInSession.findId().map(Customer::referenceOfId).orElse(null))
                .build());
    }

    private LocalizedString generateName() {
        return LocalizedString.ofEnglish("Wishlist");
    }
}
