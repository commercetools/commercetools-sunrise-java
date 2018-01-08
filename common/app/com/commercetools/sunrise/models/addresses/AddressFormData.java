package com.commercetools.sunrise.models.addresses;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.updateactions.AddAddress;
import io.sphere.sdk.models.Address;

import java.util.List;

import static java.util.Collections.singletonList;

@ImplementedBy(DefaultAddressFormData.class)
public interface AddressFormData {

    default List<UpdateAction<Customer>> updateActions() {
        return singletonList(AddAddress.of(address()));
    }

    Address address();

    boolean defaultShippingAddress();

    boolean defaultBillingAddress();

    void applyAddress(final Address address);

    void applyDefaultShippingAddress(final boolean defaultShippingAddress);

    void applyDefaultBillingAddress(final boolean defaultBillingAddress);
}
