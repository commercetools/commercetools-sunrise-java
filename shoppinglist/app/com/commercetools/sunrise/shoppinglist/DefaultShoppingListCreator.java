package com.commercetools.sunrise.shoppinglist;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.sessions.customer.CustomerInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.ShoppingListDraftBuilder;
import io.sphere.sdk.shoppinglists.ShoppingListDraftDsl;
import io.sphere.sdk.shoppinglists.commands.ShoppingListCreateCommand;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultShoppingListCreator extends AbstractShoppingListCreateExecutor implements ShoppingListCreator {
    private final CustomerInSession customerInSession;

    @Inject
    protected DefaultShoppingListCreator(final SphereClient sphereClient, final HookRunner hookRunner, final CustomerInSession customerInSession) {
        super(sphereClient, hookRunner);
        this.customerInSession = customerInSession;
    }

    public CompletionStage<ShoppingList> get(final String shoppingListType) {
        return executeRequest(buildRequest(shoppingListType));
    }

    protected ShoppingListCreateCommand buildRequest(final String shoppingListType) {
        final Reference<Customer> customer = customerInSession.findCustomerId()
                .map(Customer::referenceOfId)
                .orElse(null);

        final ShoppingListDraftDsl shoppingList = ShoppingListDraftBuilder.of(LocalizedString.ofEnglish(StringUtils.capitalize(shoppingListType)))
                .customer(customer)
                .build();

        return ShoppingListCreateCommand.of(shoppingList);
    }
}
