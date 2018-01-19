package com.commercetools.sunrise.shoppingcart.checkout;

import com.commercetools.sunrise.core.AbstractFormAction;
import com.commercetools.sunrise.models.orders.OrderCreator;
import play.data.FormFactory;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class DefaultPlaceOrderFormAction extends AbstractFormAction<PlaceOrderFormData> implements PlaceOrderFormAction {

    private final PlaceOrderFormData formData;
    private final OrderCreator orderCreator;

    @Inject
    DefaultPlaceOrderFormAction(final FormFactory formFactory, final PlaceOrderFormData formData,
                                final OrderCreator orderCreator) {
        super(formFactory);
        this.formData = formData;
        this.orderCreator = orderCreator;
    }

    @Override
    protected Class<? extends PlaceOrderFormData> formClass() {
        return formData.getClass();
    }

    @Override
    protected CompletionStage<?> onValidForm(final PlaceOrderFormData formData) {
        return orderCreator.get();
    }
}
