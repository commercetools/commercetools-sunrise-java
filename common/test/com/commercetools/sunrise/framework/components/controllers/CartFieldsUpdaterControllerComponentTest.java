package com.commercetools.sunrise.framework.components.controllers;

import com.commercetools.sunrise.framework.components.controllers.CartFieldsUpdaterControllerComponent;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetCountry;
import io.sphere.sdk.carts.commands.updateactions.SetCustomerEmail;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartFieldsUpdaterControllerComponentTest {

    private static final CountryCode CURRENT_COUNTRY = CountryCode.DE;

    @Test
    public void updatesCartWithCountryWhenNotDefined() throws Exception {
        final Cart cart = mock(Cart.class);
        when(cart.getCountry()).thenReturn(null);
        final CartUpdateCommand cartUpdateCommand = component().buildRequest(cart, null);
        assertThat(cartUpdateCommand.getUpdateActions())
                .extracting(action -> (UpdateAction<Cart>) action)
                .containsOnlyOnce(SetCountry.of(CURRENT_COUNTRY))
                .containsOnlyOnce(SetShippingAddress.of(Address.of(CURRENT_COUNTRY)));
    }

    @Test
    public void updatesCartWithCountryIfADifferentCountryDefined() throws Exception {
        final Cart cart = mock(Cart.class);
        when(cart.getCountry()).thenReturn(CountryCode.US);
        final CartUpdateCommand cartUpdateCommand = component().buildRequest(cart, null);
        assertThat(cartUpdateCommand.getUpdateActions())
                .extracting(action -> (UpdateAction<Cart>) action)
                .containsOnlyOnce(SetCountry.of(CURRENT_COUNTRY))
                .containsOnlyOnce(SetShippingAddress.of(Address.of(CURRENT_COUNTRY)));
    }

    @Test
    public void doesNotUpdatesCartWithCountryIfAlreadyDefined() throws Exception {
        final Cart cart = mock(Cart.class);
        when(cart.getCountry()).thenReturn(CURRENT_COUNTRY);
        final CartUpdateCommand cartUpdateCommand = component().buildRequest(cart, null);
        assertThat(cartUpdateCommand.getUpdateActions())
                .extracting(action -> (UpdateAction<Cart>) action)
                .doesNotContain(SetCountry.of(CURRENT_COUNTRY))
                .doesNotContain(SetShippingAddress.of(Address.of(CURRENT_COUNTRY)));
    }

    @Test
    public void updatesCartWithCustomerEmailWhenNotDefined() throws Exception {
        final Cart cart = mock(Cart.class);
        when(cart.getCustomerEmail()).thenReturn(null);
        final Customer customer = mock(Customer.class);
        when(customer.getEmail()).thenReturn("some@email.com");
        final CartUpdateCommand cartUpdateCommand = component().buildRequest(cart, customer);
        assertThat(cartUpdateCommand.getUpdateActions())
                .extracting(action -> (UpdateAction<Cart>) action)
                .containsOnlyOnce(SetCustomerEmail.of("some@email.com"));
    }

    @Test
    public void doesNotUpdateCartWithCustomerEmailIfAlreadyDefined() throws Exception {
        final Cart cart = mock(Cart.class);
        when(cart.getCustomerEmail()).thenReturn("my@email.com");
        final Customer customer = mock(Customer.class);
        when(customer.getEmail()).thenReturn("some@email.com");
        final CartUpdateCommand cartUpdateCommand = component().buildRequest(cart, customer);
        assertThat(cartUpdateCommand.getUpdateActions())
                .extracting(action -> (UpdateAction<Cart>) action)
                .doesNotContain(SetCustomerEmail.of("some@email.com"));
    }

    private CartFieldsUpdaterControllerComponent component() {
        return new CartFieldsUpdaterControllerComponent(CURRENT_COUNTRY, null);
    }
}
