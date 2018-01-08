package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.updateactions.SetBillingAddress;
import io.sphere.sdk.carts.commands.updateactions.SetCountry;
import io.sphere.sdk.carts.commands.updateactions.SetCustomerEmail;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.models.Address;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ImplementedBy(DefaultCheckoutAddressFormData.class)
public interface CheckoutAddressFormData {

    default List<UpdateAction<Cart>> updateActions() {
        final List<UpdateAction<Cart>> updateActions = new ArrayList<>();
        final Address shippingAddress = shippingAddress();
        updateActions.add(SetCountry.of(shippingAddress.getCountry()));
        updateActions.add(SetShippingAddress.of(shippingAddress));
        updateActions.add(SetBillingAddress.of(billingAddress()));
        Optional.ofNullable(shippingAddress.getEmail())
                .ifPresent(email -> updateActions.add(SetCustomerEmail.of(email)));
        return updateActions;
    }

    Address shippingAddress();

    @Nullable Address billingAddress();

    void applyCart(final Cart cart);
}
