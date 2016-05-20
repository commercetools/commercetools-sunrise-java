package shoppingcart.checkout.address;

import common.contexts.SunriseDataBeanFactory;
import common.errors.ErrorsBean;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Address;
import play.data.Form;

import javax.inject.Inject;

import static common.utils.FormUtils.extractAddress;
import static common.utils.FormUtils.extractBooleanFormField;

public class CheckoutAddressPageContentFactory extends SunriseDataBeanFactory {
    @Inject
    private CheckoutAddressFormBeanFactory formBeanFactory;

    public CheckoutAddressPageContent create(final Cart cart) {
        final CheckoutAddressPageContent pageContent = new CheckoutAddressPageContent();
        pageContent.setAddressForm(formBeanFactory.create(cart));
        return pageContent;
    }

    protected CheckoutAddressPageContent createWithAddressError(final Form<CheckoutShippingAddressFormData> shippingAddressForm,
                                                                final Form<CheckoutBillingAddressFormData> billingAddressForm,
                                                                final ErrorsBean errors) {
        final CheckoutAddressPageContent pageContent = new CheckoutAddressPageContent();
        final Address shippingAddress = extractAddress(shippingAddressForm, "Shipping");
        final Address billingAddress = extractAddress(billingAddressForm, "Billing");
        final boolean differentBillingAddress = extractBooleanFormField(shippingAddressForm, "billingAddressDifferentToBillingAddress");
        final CheckoutAddressFormBean formBean = new CheckoutAddressFormBean(shippingAddress, billingAddress, differentBillingAddress, userContext, projectContext, i18nResolver, configuration);
        formBean.setErrors(errors);
        pageContent.setAddressForm(formBean);
        return pageContent;
    }
}
