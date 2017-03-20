package com.commercetools.sunrise.framework.reverserouters.myaccount.myorders;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
import io.sphere.sdk.orders.Order;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;

@RequestScoped
public class DefaultMyOrdersReverseRouter extends AbstractLocalizedReverseRouter implements MyOrdersReverseRouter {

    private final SimpleMyOrdersReverseRouter delegate;

    @Inject
    protected DefaultMyOrdersReverseRouter(final Locale locale, final SimpleMyOrdersReverseRouter reverseRouter) {
        super(locale);
        this.delegate = reverseRouter;
    }

    @Override
    public Call myOrderListPageCall(final String languageTag) {
        return delegate.myOrderListPageCall(languageTag);
    }

    @Override
    public Call myOrderDetailPageCall(final String languageTag, final String orderIdentifier) {
        return delegate.myOrderDetailPageCall(languageTag, orderIdentifier);
    }

    /**
     * {@inheritDoc}
     * It uses as order identifier the order number.
     */
    @Override
    public Optional<Call> myOrderDetailPageCall(final Order order) {
        return Optional.ofNullable(order.getOrderNumber())
                .map(this::myOrderDetailPageCall);
    }
}
