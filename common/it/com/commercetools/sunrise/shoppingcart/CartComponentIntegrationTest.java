package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.WithSphereClient;
import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerSignInResult;
import org.junit.Before;
import org.junit.Test;

import static com.commercetools.sunrise.common.TestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CartComponentIntegrationTest extends WithSphereClient {

    private Injector injector;

    @Before
    public void setUp() throws Exception {
        this.injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(SphereClient.class).toInstance(sphereClient());
                bindScope(RequestScoped.class, Scopes.NO_SCOPE);
            }
        });
    }

    @Test
    public void updatesCartWithCustomerEmailWhenNotDefined() throws Exception {
        withCustomer(sphereClient(), customerDraft(), customerCreatedResult -> {
            final Customer customer = customerCreatedResult.getCustomer();
            withCart(sphereClient(), cartDraft(), anonymousCart -> {
                final CustomerSignInResult signInResult = logInCustomer(sphereClient(), customer, anonymousCart);
                final Cart updatedCart = invokeUpdateCartWithMissingInfoOnSignIn(signInResult);
                assertThat(anonymousCart.getCustomerEmail()).isNull();
                assertThat(updatedCart.getCustomerEmail()).isEqualTo(customer.getEmail());
                return updatedCart;
            });
            return customer;
        });
    }

    @Test
    public void doesNotUpdateCartWithCustomerEmailIfAlreadyDefined() throws Exception {
        withCustomer(sphereClient(), customerDraft(), customerCreatedResult -> {
            final Customer customer = customerCreatedResult.getCustomer();
            withCart(sphereClient(), cartDraft().withCustomerEmail("anonymous-cart@email.com"), anonymousCart -> {
                final CustomerSignInResult signInResult = logInCustomer(sphereClient(), customer, anonymousCart);
                final Cart updatedCart = invokeUpdateCartWithMissingInfoOnSignIn(signInResult);
                assertThat(updatedCart.getCustomerEmail())
                        .isEqualTo(anonymousCart.getCustomerEmail())
                        .isEqualTo("anonymous-cart@email.com");
                return updatedCart;
            });
            return customer;
        });
    }

    @Test
    public void registeredCartCustomerEmailPrevails() throws Exception {
        withCart(sphereClient(), cartDraft().withCustomerEmail("registered-cart@email.com"), registeredCart -> {
            withCustomer(sphereClient(), customerDraft().withCart(registeredCart), customerCreatedResult -> {
                final Customer customer = customerCreatedResult.getCustomer();
                withCart(sphereClient(), cartDraft().withCustomerEmail("anonymous-cart@email.com"), anonymousCart -> {
                    final CustomerSignInResult signInResult = logInCustomer(sphereClient(), customer, anonymousCart);
                    final Cart updatedCart = invokeUpdateCartWithMissingInfoOnSignIn(signInResult);
                    assertThat(updatedCart.getCustomerEmail())
                            .isEqualTo(registeredCart.getCustomerEmail())
                            .isEqualTo("registered-cart@email.com");
                    return anonymousCart;
                });
                return customer;
            });
            return registeredCart;
        });
    }

    private Cart invokeUpdateCartWithMissingInfoOnSignIn(final CustomerSignInResult customerSignInResult) {
        return injector.getInstance(CartComponent.class)
                .updateCartWithMissingInfoOnSignIn(customerSignInResult.getCart(), customerSignInResult.getCustomer())
                .toCompletableFuture().join();
    }

}
