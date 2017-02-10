package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import io.sphere.sdk.orders.Order;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;

@RequestScoped
final class ReflectionMyOrdersLocalizedReverseRouter extends AbstractLocalizedReverseRouter implements MyOrdersLocalizedReverseRouter {

    private final MyOrdersReverseRouter delegate;

    @Inject
    private ReflectionMyOrdersLocalizedReverseRouter(final Locale locale, final MyOrdersReverseRouter reverseRouter) {
        super(locale);
        this.delegate = reverseRouter;
    }

    @Override
    public Call myOrderListPageCall(final String languageTag) {
        return delegate.myOrderListPageCall(languageTag);
    }

    @Override
    public Call myOrderDetailPageCall(final String languageTag, final String orderNumber) {
        return delegate.myOrderDetailPageCall(languageTag, orderNumber);
    }

    @Override
    public Optional<Call> myOrderDetailPageCall(final Locale locale, final Order order) {
        return delegate.myOrderDetailPageCall(locale, order);
    }

    @Override
    public String myOrderDetailPageUrlOrEmpty(final Locale locale, final Order order) {
        return delegate.myOrderDetailPageUrlOrEmpty(locale, order);
    }
}
