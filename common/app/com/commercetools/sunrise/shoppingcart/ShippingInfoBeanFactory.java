package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.CartPriceUtils;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.PriceUtils;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

@RequestScoped
public class ShippingInfoBeanFactory extends ViewModelFactory<ShippingInfoBean, ShippingInfoBeanFactory.Data> {

    private final CurrencyUnit currency;
    private final PriceFormatter priceFormatter;

    @Inject
    public ShippingInfoBeanFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter) {
        this.currency = currency;
        this.priceFormatter = priceFormatter;
    }

    public final ShippingInfoBean create(@Nullable final CartLike<?> cartLike) {
        final Data data = new Data(cartLike);
        return initializedViewModel(data);
    }

    @Override
    protected ShippingInfoBean getViewModelInstance() {
        return new ShippingInfoBean();
    }

    @Override
    protected final void initialize(final ShippingInfoBean bean, final Data data) {
        fillLabel(bean, data);
        fillDescription(bean, data);
        fillPrice(bean, data);
    }

    protected void fillLabel(final ShippingInfoBean bean, final Data data) {
        if (data.cartLike != null && data.cartLike.getShippingInfo() != null) {
            bean.setLabel(data.cartLike.getShippingInfo().getShippingMethodName());
        }
    }

    protected void fillDescription(final ShippingInfoBean bean, final Data data) {
        if (data.cartLike != null && data.cartLike.getShippingInfo() != null) {
            final Reference<ShippingMethod> ref = data.cartLike.getShippingInfo().getShippingMethod();
            if (ref != null && ref.getObj() != null) {
                bean.setDescription(ref.getObj().getDescription());
            }
        }
    }

    protected void fillPrice(final ShippingInfoBean bean, final Data data) {
        if (data.cartLike != null) {
            final MonetaryAmount amount = CartPriceUtils.calculateAppliedShippingPrice(data.cartLike)
                    .orElseGet(() -> zeroAmount(data.cartLike.getCurrency()));
            bean.setPrice(priceFormatter.format(amount));
        } else {
            bean.setPrice(priceFormatter.format(zeroAmount(currency)));
        }
    }

    private static MonetaryAmount zeroAmount(final CurrencyUnit currency) {
        return PriceUtils.zeroAmount(currency);
    }

    protected final static class Data extends Base {

        @Nullable
        public final CartLike<?> cartLike;

        public Data(@Nullable final CartLike<?> cartLike) {
            this.cartLike = cartLike;
        }
    }
}
