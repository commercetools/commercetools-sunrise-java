package com.commercetools.sunrise.shoppingcart.checkout;

import com.commercetools.sunrise.core.controllers.AbstractFormAction;
import com.commercetools.sunrise.models.carts.MyCartUpdater;
import play.data.FormFactory;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class DefaultCheckoutShippingFormAction extends AbstractFormAction<CheckoutShippingFormData> implements CheckoutShippingFormAction {

    private final CheckoutShippingFormData formData;
    private final MyCartUpdater myCartUpdater;

    @Inject
    DefaultCheckoutShippingFormAction(final FormFactory formFactory, final CheckoutShippingFormData formData,
                                      final MyCartUpdater myCartUpdater) {
        super(formFactory);
        this.formData = formData;
        this.myCartUpdater = myCartUpdater;
    }

    @Override
    protected Class<? extends CheckoutShippingFormData> formClass() {
        return formData.getClass();
    }

    @Override
    protected CompletionStage<?> onValidForm(final CheckoutShippingFormData formData) {
        return myCartUpdater.force(formData.setShippingMethod());
    }
}
