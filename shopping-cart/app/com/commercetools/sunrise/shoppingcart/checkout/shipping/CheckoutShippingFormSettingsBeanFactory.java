package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.forms.FormFieldWithOptions;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.Configuration;

@RequestScoped
public class CheckoutShippingFormSettingsBeanFactory extends ViewModelFactory<CheckoutShippingFormSettingsBean, CheckoutShippingControllerData> {

    private final String shippingFormFieldName;
    private final ShippingMethodFormFieldBeanFactory shippingMethodFormFieldBeanFactory;

    public CheckoutShippingFormSettingsBeanFactory(final Configuration configuration, final ShippingMethodFormFieldBeanFactory shippingMethodFormFieldBeanFactory) {
        this.shippingFormFieldName =  configuration.getString("checkout.shipping.formFieldName", "shippingMethodId");;
        this.shippingMethodFormFieldBeanFactory = shippingMethodFormFieldBeanFactory;
    }

    @Override
    protected CheckoutShippingFormSettingsBean getViewModelInstance() {
        return new CheckoutShippingFormSettingsBean();
    }

    @Override
    public final CheckoutShippingFormSettingsBean create(final CheckoutShippingControllerData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final CheckoutShippingFormSettingsBean model, final CheckoutShippingControllerData data) {
        fillPaymentMethod(model, data);
    }

    protected void fillPaymentMethod(final CheckoutShippingFormSettingsBean model, final CheckoutShippingControllerData data) {
        final FormFieldWithOptions<ShippingMethod> formFieldWithOptions = new FormFieldWithOptions<>(data.form.field(shippingFormFieldName), data.shippingMethods);
        model.setShippingMethod(shippingMethodFormFieldBeanFactory.create(formFieldWithOptions));
    }
}
