package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.MyCustomerInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.shoppinglists.ShoppingListDraftBuilder;
import io.sphere.sdk.shoppinglists.commands.ShoppingListCreateCommand;

import javax.inject.Inject;

public final class DefaultMyWishlistCreator extends AbstractMyWishlistCreator {

    private final MyCustomerInSession myCustomerInSession;

    @Inject
    DefaultMyWishlistCreator(final SphereClient sphereClient, final HookRunner hookRunner,
                             final MyWishlist myWishlist, final MyCustomerInSession myCustomerInSession) {
        super(hookRunner, sphereClient, myWishlist);
        this.myCustomerInSession = myCustomerInSession;
    }

    @Override
    protected ShoppingListCreateCommand buildRequest() {
        return ShoppingListCreateCommand.of(ShoppingListDraftBuilder.of(generateName())
                .customer(myCustomerInSession.findId().map(Customer::referenceOfId).orElse(null))
                .build());
    }

    private LocalizedString generateName() {
        return LocalizedString.ofEnglish("Wishlist");
    }
}
