package com.commercetools.sunrise.shoppingcart.checkout;

import com.commercetools.sunrise.core.controllers.AbstractFormAction;
import com.commercetools.sunrise.models.carts.MyCartUpdater;
import play.data.FormFactory;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class DefaultSetShippingFormAction extends AbstractFormAction<SetShippingFormData> implements SetShippingFormAction {

    private final SetShippingFormData formData;
    private final MyCartUpdater myCartUpdater;

    @Inject
    DefaultSetShippingFormAction(final FormFactory formFactory, final SetShippingFormData formData,
                                 final MyCartUpdater myCartUpdater) {
        super(formFactory);
        this.formData = formData;
        this.myCartUpdater = myCartUpdater;
    }

    @Override
    protected Class<? extends SetShippingFormData> formClass() {
        return formData.getClass();
    }

    @Override
    protected CompletionStage<?> onValidForm(final SetShippingFormData formData) {
        return myCartUpdater.force(formData.setShippingMethod());
    }
}
