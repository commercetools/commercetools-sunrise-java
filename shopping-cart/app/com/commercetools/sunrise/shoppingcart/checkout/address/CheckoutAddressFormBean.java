package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.neovisionaries.i18n.CountryCode;
import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.errors.ErrorsBean;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.Base;
import play.Configuration;

import javax.annotation.Nullable;
import java.util.List;

import static java.util.Collections.singletonList;

public class CheckoutAddressFormBean extends Base {

    private boolean differentBillingAddress;
    private AddressFormBean shippingAddress;
    private AddressFormBean billingAddress;
    private ErrorsBean errors;

    public CheckoutAddressFormBean() {
    }

    public CheckoutAddressFormBean(final @Nullable Address shippingAddress, final @Nullable Address billingAddress,
                                   final boolean differentBillingAddress, final UserContext userContext,
                                   final ProjectContext projectContext, final I18nResolver i18nResolver, final Configuration configuration) {
        this.shippingAddress = createShippingAddressFormBean(shippingAddress, userContext, i18nResolver, configuration);
        this.billingAddress = createBillingAddressFormBean(billingAddress, userContext, projectContext, i18nResolver, configuration);
        this.differentBillingAddress = differentBillingAddress;
    }

    public boolean isBillingAddressDifferentToBillingAddress() {
        return differentBillingAddress;
    }

    public void setBillingAddressDifferentToBillingAddress(final boolean billingAddressDifferentToBillingAddress) {
        this.differentBillingAddress = billingAddressDifferentToBillingAddress;
    }

    public AddressFormBean getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(final AddressFormBean shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public AddressFormBean getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(final AddressFormBean billingAddress) {
        this.billingAddress = billingAddress;
    }

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(final ErrorsBean errors) {
        this.errors = errors;
    }

    private List<CountryCode> availableBillingCountries(final ProjectContext projectContext) {
        return projectContext.countries();
    }

    @Deprecated
    public static AddressFormBean createShippingAddressFormBean(final @Nullable Address shippingAddress,
                                                                 final UserContext userContext, final I18nResolver i18nResolver,
                                                                 final Configuration configuration) {
        final List<CountryCode> shippingCountries = singletonList(userContext.country());
        return new AddressFormBean(shippingAddress, shippingCountries, userContext, i18nResolver, configuration);
    }

    @Deprecated
    public static AddressFormBean createBillingAddressFormBean(final @Nullable Address billingAddress,
                                                                final UserContext userContext, final ProjectContext projectContext,
                                                                final I18nResolver i18nResolver, final Configuration configuration) {
        final List<CountryCode> billingCountries = projectContext.countries();
        return new AddressFormBean(billingAddress, billingCountries, userContext, i18nResolver, configuration);
    }
}
