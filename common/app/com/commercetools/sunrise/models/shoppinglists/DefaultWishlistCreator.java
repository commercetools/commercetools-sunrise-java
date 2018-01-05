package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.CustomerInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shoppinglists.ShoppingListDraftBuilder;
import io.sphere.sdk.shoppinglists.ShoppingListDraftDsl;
import io.sphere.sdk.shoppinglists.commands.ShoppingListCreateCommand;

import javax.inject.Inject;
import java.util.Optional;

public class DefaultWishlistCreator extends AbstractShoppingListCreateExecutor {

    private final CustomerInSession customerInSession;

    @Inject
    protected DefaultWishlistCreator(final SphereClient sphereClient, final HookRunner hookRunner, final CustomerInSession customerInSession) {
        super(sphereClient, hookRunner);
        this.customerInSession = customerInSession;
    }

    @Override
    protected ShoppingListCreateCommand buildRequest() {
        return ShoppingListCreateCommand.of(buildDraft());
    }

    private ShoppingListDraftDsl buildDraft() {
        return ShoppingListDraftBuilder.of(LocalizedString.ofEnglish("Wishlist"))
                .customer(customerReference().orElse(null))
                .build();
    }

    private Optional<Reference<Customer>> customerReference() {
        return customerInSession.findId().map(Customer::referenceOfId);
    }
}
