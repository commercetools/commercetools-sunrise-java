package com.commercetools.sunrise.shoppingcart.checkout;

import com.commercetools.sunrise.core.AbstractFormAction;
import com.commercetools.sunrise.models.orders.OrderCreator;
import io.sphere.sdk.orders.Order;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Http;

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
        return orderCreator.get().thenApplyAsync(this::saveOrderNumber, HttpExecution.defaultContext());
    }

    private String saveOrderNumber(final Order order) {
        return Http.Context.current().flash().put("orderNumber", order.getOrderNumber());
    }
}
