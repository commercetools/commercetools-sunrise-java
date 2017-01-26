package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.forms.FormFieldWithOptions;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.Configuration;

@RequestScoped
public class CheckoutShippingFormSettingsBeanFactory extends ViewModelFactory<CheckoutShippingFormSettingsBean, CheckoutShippingPageData> {

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
    public final CheckoutShippingFormSettingsBean create(final CheckoutShippingPageData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final CheckoutShippingFormSettingsBean model, final CheckoutShippingPageData data) {
        fillPaymentMethod(model, data);
    }

    protected void fillPaymentMethod(final CheckoutShippingFormSettingsBean model, final CheckoutShippingPageData data) {
        final FormFieldWithOptions<ShippingMethod> formFieldWithOptions = new FormFieldWithOptions<>(data.form.field(shippingFormFieldName), data.shippingMethods);
        model.setShippingMethod(shippingMethodFormFieldBeanFactory.create(formFieldWithOptions));
    }
}
