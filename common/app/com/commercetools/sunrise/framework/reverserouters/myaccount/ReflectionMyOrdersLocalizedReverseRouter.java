package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
import io.sphere.sdk.orders.Order;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;

@RequestScoped
final class ReflectionMyOrdersLocalizedReverseRouter extends AbstractLocalizedReverseRouter implements MyOrdersReverseRouter {

    private final MyOrdersSimpleReverseRouter delegate;

    @Inject
    private ReflectionMyOrdersLocalizedReverseRouter(final Locale locale, final MyOrdersSimpleReverseRouter reverseRouter) {
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
