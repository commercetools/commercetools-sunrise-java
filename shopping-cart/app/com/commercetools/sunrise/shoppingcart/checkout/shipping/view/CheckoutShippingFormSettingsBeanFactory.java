package com.commercetools.sunrise.shoppingcart.checkout.shipping.view;

import com.commercetools.sunrise.common.forms.FormFieldWithOptions;
import com.commercetools.sunrise.common.models.FormViewModelFactory;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.CheckoutShippingFormData;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.ShippingMethodsWithCart;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.Configuration;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutShippingFormSettingsBeanFactory extends FormViewModelFactory<CheckoutShippingFormSettingsBean, ShippingMethodsWithCart, CheckoutShippingFormData> {

    private final String shippingFormFieldName;
    private final ShippingMethodFormFieldBeanFactory shippingMethodFormFieldBeanFactory;

    @Inject
    public CheckoutShippingFormSettingsBeanFactory(final Configuration configuration, final ShippingMethodFormFieldBeanFactory shippingMethodFormFieldBeanFactory) {
        this.shippingFormFieldName =  configuration.getString("checkout.shipping.formFieldName", "shippingMethodId");;
        this.shippingMethodFormFieldBeanFactory = shippingMethodFormFieldBeanFactory;
    }

    @Override
    protected CheckoutShippingFormSettingsBean getViewModelInstance() {
        return new CheckoutShippingFormSettingsBean();
    }

    @Override
    public final CheckoutShippingFormSettingsBean create(final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        return super.create(shippingMethodsWithCart, form);
    }

    @Override
    protected final void initialize(final CheckoutShippingFormSettingsBean model, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        fillPaymentMethod(model, shippingMethodsWithCart, form);
    }

    protected void fillPaymentMethod(final CheckoutShippingFormSettingsBean model, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        final FormFieldWithOptions<ShippingMethod> formFieldWithOptions = new FormFieldWithOptions<>(form.field(shippingFormFieldName), shippingMethodsWithCart.getShippingMethods());
        model.setShippingMethod(shippingMethodFormFieldBeanFactory.create(formFieldWithOptions));
    }
}
