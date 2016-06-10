package shoppingcart.checkout.address;

import com.commercetools.sunrise.common.contexts.SunriseDataBeanFactory;
import io.sphere.sdk.carts.Cart;

import javax.inject.Inject;

public class CheckoutAddressFormBeanFactory extends SunriseDataBeanFactory {
    @Inject
    private AddressFormBeanFactory addressFormBeanFactory;

    public CheckoutAddressFormBean create(final Cart cart) {
        final CheckoutAddressFormBean bean = new CheckoutAddressFormBean();
        bean.setShippingAddress(addressFormBeanFactory.createShippingAddressFormBean(cart.getShippingAddress()));
        bean.setBillingAddress(addressFormBeanFactory.createBillingAddressFormBean(cart.getBillingAddress()));
        bean.setBillingAddressDifferentToBillingAddress(cart.getBillingAddress() != null);
        return bean;
    }
}
