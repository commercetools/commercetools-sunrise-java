package com.commercetools.sunrise.framework.checkout.shipping.viewmodels;

import com.commercetools.sunrise.common.forms.FormFieldWithOptions;
import com.commercetools.sunrise.common.models.FormViewModelFactory;
import com.commercetools.sunrise.framework.checkout.shipping.CheckoutShippingFormData;
import com.commercetools.sunrise.framework.checkout.shipping.ShippingMethodsWithCart;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.Configuration;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutShippingFormSettingsViewModelFactory extends FormViewModelFactory<CheckoutShippingFormSettingsViewModel, ShippingMethodsWithCart, CheckoutShippingFormData> {

    private final String shippingFormFieldName;
    private final ShippingMethodFormFieldViewModelFactory shippingMethodFormFieldViewModelFactory;

    @Inject
    public CheckoutShippingFormSettingsViewModelFactory(final Configuration configuration, final ShippingMethodFormFieldViewModelFactory shippingMethodFormFieldViewModelFactory) {
        this.shippingFormFieldName =  configuration.getString("checkout.shipping.formFieldName", "shippingMethodId");;
        this.shippingMethodFormFieldViewModelFactory = shippingMethodFormFieldViewModelFactory;
    }

    @Override
    protected CheckoutShippingFormSettingsViewModel getViewModelInstance() {
        return new CheckoutShippingFormSettingsViewModel();
    }

    @Override
    public final CheckoutShippingFormSettingsViewModel create(final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        return super.create(shippingMethodsWithCart, form);
    }

    @Override
    protected final void initialize(final CheckoutShippingFormSettingsViewModel model, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        fillPaymentMethod(model, shippingMethodsWithCart, form);
    }

    protected void fillPaymentMethod(final CheckoutShippingFormSettingsViewModel model, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        final FormFieldWithOptions<ShippingMethod> formFieldWithOptions = new FormFieldWithOptions<>(form.field(shippingFormFieldName), shippingMethodsWithCart.getShippingMethods());
        model.setShippingMethod(shippingMethodFormFieldViewModelFactory.create(formFieldWithOptions));
    }
}
