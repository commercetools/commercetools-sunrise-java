package com.commercetools.sunrise.it;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.customers.*;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import io.sphere.sdk.customers.commands.CustomerDeleteCommand;
import io.sphere.sdk.customers.commands.CustomerSignInCommand;

import javax.annotation.Nullable;
import java.util.function.Function;

import static com.commercetools.sunrise.it.TestFixtures.*;

public final class CustomerTestFixtures {

    private CustomerTestFixtures() {
    }

    public static void withCustomer(final BlockingSphereClient client, final CustomerDraft customerDraft, final Function<CustomerSignInResult, Customer> test) {
        final CustomerSignInResult customerSignInResult = client.executeBlocking(CustomerCreateCommand.of(customerDraft));
        final Customer customerAfterTest = test.apply(customerSignInResult);
        deleteCustomerWithRetry(client, customerAfterTest);
    }

    public static CustomerSignInResult logInCustomer(final BlockingSphereClient client, final Customer customer, @Nullable final Cart cart) {
        final String cartId = cart != null ? cart.getId() : null;
        return client.executeBlocking(CustomerSignInCommand.of(customer.getEmail(), password(), cartId));
    }

    public static CustomerDraftDsl customerDraft() {
        return CustomerDraftBuilder.of(randomEmail(CustomerTestFixtures.class), password()).build();
    }

    public static void deleteCustomerWithRetry(final BlockingSphereClient client, final Customer customerAfterTest) {
        deleteWithRetry(client, customerAfterTest, CustomerDeleteCommand::of, DEFAULT_DELETE_TTL);
    }

    public static String password() {
        return "PSW-12345";
    }
}
