package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.it.WithSphereClient;
import com.commercetools.sunrise.sessions.customer.CustomerInSession;
import com.commercetools.sunrise.sessions.customer.UserInfoBean;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerSignInResult;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.Optional;

import static com.commercetools.sunrise.it.TestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CartFieldsUpdaterControllerComponentIntegrationTest extends WithSphereClient {

    @Test
    public void updatesCartWithCustomerEmailWhenNotDefined() throws Exception {
        withCustomer(sphereClient(), customerDraft(), customerCreatedResult -> {
            final Customer customer = customerCreatedResult.getCustomer();
            withCart(sphereClient(), cartDraft(), anonymousCart -> {
                final CustomerSignInResult signInResult = logInCustomer(sphereClient(), customer, anonymousCart);
                final Cart updatedCart = runComponent(signInResult);
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
                final Cart updatedCart = runComponent(signInResult);
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
                    final Cart updatedCart = runComponent(signInResult);
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

    private Cart runComponent(final CustomerSignInResult customerSignInResult) {
        return new CartFieldsUpdaterControllerComponent(CountryCode.DE, getCustomerInSession(), sphereClient())
                .onCustomerSignedInAction(customerSignInResult, null).toCompletableFuture().join().getCart();
    }

    private CustomerInSession getCustomerInSession() {
        return new CustomerInSession() {
            @Override
            public Optional<String> findCustomerId() {
                return Optional.of("customer-id");
            }

            @Override
            public Optional<String> findCustomerEmail() {
                return Optional.of("customer-email");
            }

            @Override
            public Optional<UserInfoBean> findUserInfo() {
                return null;
            }

            @Override
            public void store(@Nullable final Customer customer) {

            }

            @Override
            public void remove() {

            }
        };
    }

}
