package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.forms.AddressFormBean;
import com.commercetools.sunrise.common.forms.AddressFormBeanFactory;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Address;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.singletonList;

public class CheckoutAddressFormBeanFactory extends AddressFormBeanFactory {

    @Inject
    private UserContext userContext;

    public CheckoutAddressFormBean create(final Cart cart) {
        final CheckoutAddressFormBean bean = new CheckoutAddressFormBean();
        bean.setShippingAddress(createShippingForm(cart.getShippingAddress()));
        bean.setBillingAddress(createBillingForm(cart.getBillingAddress()));
        bean.setBillingAddressDifferentToBillingAddress(cart.getBillingAddress() != null);
        return bean;
    }

    protected AddressFormBean createShippingForm(@Nullable final Address shippingAddress) {
        final AddressFormBean bean = new AddressFormBean();
        final List<CountryCode> shippingCountries = singletonList(userContext.country());
        fillAddressForm(bean, shippingAddress, shippingCountries);
        return bean;
    }

    protected AddressFormBean createBillingForm(@Nullable final Address billingAddress) {
        return create(billingAddress);
    }
}
