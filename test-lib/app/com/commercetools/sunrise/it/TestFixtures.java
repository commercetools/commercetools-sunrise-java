package com.commercetools.sunrise.it;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftDsl;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.carts.commands.CartDeleteCommand;
import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.client.ConcurrentModificationException;
import io.sphere.sdk.client.NotFoundException;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.customers.*;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import io.sphere.sdk.customers.commands.CustomerDeleteCommand;
import io.sphere.sdk.customers.commands.CustomerSignInCommand;
import io.sphere.sdk.models.DefaultCurrencyUnits;
import io.sphere.sdk.models.Versioned;

import javax.annotation.Nullable;
import java.util.function.Function;

import static io.sphere.sdk.test.SphereTestUtils.randomEmail;

public class TestFixtures {

    public static String CUSTOMER_PASSWORD = "PSW-12345";
    private static final int DEFAULT_DELETE_TTL = 5;

    public static void withCart(final BlockingSphereClient client, final CartDraft cartDraft, final Function<Cart, Cart> test) {
        final Cart cart = client.executeBlocking(CartCreateCommand.of(cartDraft));
        final Cart cartAfterTest = test.apply(cart);
        deleteCartWithRetry(client, cartAfterTest);
    }

    public static void withCustomer(final BlockingSphereClient client, final CustomerDraft customerDraft, final Function<CustomerSignInResult, Customer> test) {
        final CustomerSignInResult customerSignInResult = client.executeBlocking(CustomerCreateCommand.of(customerDraft));
        final Customer customerAfterTest = test.apply(customerSignInResult);
        deleteCustomerWithRetry(client, customerAfterTest);
    }

    public static CustomerSignInResult logInCustomer(final BlockingSphereClient client, final Customer customer, @Nullable final Cart cart) {
        final String cartId = cart != null ? cart.getId() : null;
        return client.executeBlocking(CustomerSignInCommand.of(customer.getEmail(), CUSTOMER_PASSWORD, cartId));
    }

    public static CartDraftDsl cartDraft() {
        return CartDraft.of(DefaultCurrencyUnits.EUR);
    }

    public static CustomerDraftDsl customerDraft() {
        return CustomerDraftBuilder.of(randomEmail(TestFixtures.class), TestFixtures.CUSTOMER_PASSWORD).build();
    }

    private static void deleteCartWithRetry(final BlockingSphereClient client, final Cart cartAfterTest) {
        deleteWithRetry(client, cartAfterTest, CartDeleteCommand::of, DEFAULT_DELETE_TTL);
    }

    private static void deleteCustomerWithRetry(final BlockingSphereClient client, final Customer customerAfterTest) {
        deleteWithRetry(client, customerAfterTest, CustomerDeleteCommand::of, DEFAULT_DELETE_TTL);
    }

    private static <T> void deleteWithRetry(final BlockingSphereClient client, final Versioned<T> resource,
                                            final Function<Versioned<T>, SphereRequest<T>> deleteFunction, final int ttl) {
        if (ttl > 0) {
            try {
                client.executeBlocking(deleteFunction.apply(resource));
            } catch (final ConcurrentModificationException e) {
                if (e.getCurrentVersion() != null) {
                    final Versioned<T> resourceWithCurrentVersion = Versioned.of(resource.getId(), e.getCurrentVersion());
                    deleteWithRetry(client, resourceWithCurrentVersion, deleteFunction, ttl - 1);
                }
            } catch (final NotFoundException e) {
                // mission indirectly accomplished
            }
        } else {
            throw new RuntimeException("Could not delete resource due to too many concurrent updates, resource: " + resource);
        }
    }
}
