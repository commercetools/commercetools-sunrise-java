package com.commercetools.sunrise.wishlist.controllers;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.sessions.customer.CustomerInSession;
import com.commercetools.sunrise.wishlist.WishlistCreator;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.ShoppingListDraftBuilder;
import io.sphere.sdk.shoppinglists.ShoppingListDraftDsl;
import io.sphere.sdk.shoppinglists.commands.ShoppingListCreateCommand;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultWishlistCreator extends AbstractShoppingListCreateExecutor implements WishlistCreator {
    private final CustomerInSession customerInSession;

    @Inject
    protected DefaultWishlistCreator(final SphereClient sphereClient, final HookRunner hookRunner, final CustomerInSession customerInSession) {
        super(sphereClient, hookRunner);
        this.customerInSession = customerInSession;
    }

    public CompletionStage<ShoppingList> get() {
        return executeRequest(buildRequest());
    }

    protected ShoppingListCreateCommand buildRequest() {
        final Reference<Customer> customer = customerInSession.findCustomerId()
                .map(Customer::referenceOfId)
                .orElse(null);

        final ShoppingListDraftDsl wishlist = ShoppingListDraftBuilder.of(LocalizedString.ofEnglish("Wishlist"))
                .customer(customer)
                .build();

        return ShoppingListCreateCommand.of(wishlist);
    }
}
