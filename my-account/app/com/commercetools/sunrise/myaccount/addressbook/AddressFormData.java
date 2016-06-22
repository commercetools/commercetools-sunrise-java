package com.commercetools.sunrise.myaccount.addressbook;

import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;

public abstract class AddressFormData extends Base {

    public abstract void apply(@Nullable final Address address);

    public abstract Address extractAddress();

    public abstract boolean isDefaultShippingAddress();

    public abstract boolean isDefaultBillingAddress();
}
