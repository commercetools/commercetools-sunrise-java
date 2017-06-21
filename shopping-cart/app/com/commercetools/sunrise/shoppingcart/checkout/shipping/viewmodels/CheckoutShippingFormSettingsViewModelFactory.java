package com.commercetools.sunrise.shoppingcart.checkout.shipping.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.forms.FormFieldWithOptions;
import com.commercetools.sunrise.framework.viewmodels.FormViewModelFactory;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.CheckoutShippingFormData;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.ShippingMethodsWithCart;
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

    protected final String getShippingFormFieldName() {
        return shippingFormFieldName;
    }

    protected final ShippingMethodFormFieldViewModelFactory getShippingMethodFormFieldViewModelFactory() {
        return shippingMethodFormFieldViewModelFactory;
    }

    @Override
    protected CheckoutShippingFormSettingsViewModel newViewModelInstance(final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        return new CheckoutShippingFormSettingsViewModel();
    }

    @Override
    public final CheckoutShippingFormSettingsViewModel create(final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        return super.create(shippingMethodsWithCart, form);
    }

    @Override
    protected final void initialize(final CheckoutShippingFormSettingsViewModel viewModel, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        fillPaymentMethod(viewModel, shippingMethodsWithCart, form);
    }

    protected void fillPaymentMethod(final CheckoutShippingFormSettingsViewModel viewModel, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        final FormFieldWithOptions<ShippingMethod> formFieldWithOptions = FormFieldWithOptions.of(form.field(shippingFormFieldName), shippingMethodsWithCart.getShippingMethods());
        viewModel.setShippingMethod(shippingMethodFormFieldViewModelFactory.create(formFieldWithOptions));
    }
}
