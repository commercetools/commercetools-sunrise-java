package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.forms.AddressFormBean;
import com.commercetools.sunrise.common.forms.AddressFormBeanFactory;
import com.neovisionaries.i18n.CountryCode;
import play.data.Form;

import javax.inject.Inject;
import java.util.List;

import static com.commercetools.sunrise.common.forms.FormUtils.extractAddress;
import static com.commercetools.sunrise.common.forms.FormUtils.extractBooleanFormField;
import static java.util.Collections.singletonList;

public class CheckoutAddressFormBeanFactory extends AddressFormBeanFactory {

    @Inject
    private UserContext userContext;

    public CheckoutAddressFormBean create(final Form<?> form) {
        final CheckoutAddressFormBean formBean = new CheckoutAddressFormBean();
        fillShippingAddress(formBean, form);
        fillBillingAddress(formBean, form);
        fillBillingDifferentThanShipping(formBean, form);
        return formBean;
    }

    protected void fillBillingDifferentThanShipping(final CheckoutAddressFormBean formBean, final Form<?> form) {
        final boolean isBillingDifferentThanShipping = extractBooleanFormField(form, "billingAddressDifferentToBillingAddress");
        formBean.setBillingAddressDifferentToBillingAddress(isBillingDifferentThanShipping);
    }

    protected void fillShippingAddress(final CheckoutAddressFormBean formBean, final Form<?> form) {
        final AddressFormBean bean = new AddressFormBean();
        final List<CountryCode> shippingCountries = singletonList(userContext.country());
        fillAddressForm(bean, extractAddress(form, "Shipping"), shippingCountries);
        formBean.setShippingAddress(bean);
    }

    protected void fillBillingAddress(final CheckoutAddressFormBean formBean, final Form<?> form) {
        final AddressFormBean bean = create(extractAddress(form, "Billing"));
        formBean.setBillingAddress(bean);
    }
}
