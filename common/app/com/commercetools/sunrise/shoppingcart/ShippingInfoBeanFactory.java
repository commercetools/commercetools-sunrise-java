package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.CartPriceUtils;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.PriceUtils;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

@RequestScoped
public class ShippingInfoBeanFactory extends ViewModelFactory<ShippingInfoBean, CartLike<?>> {

    private final CurrencyUnit currency;
    private final PriceFormatter priceFormatter;

    @Inject
    public ShippingInfoBeanFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter) {
        this.currency = currency;
        this.priceFormatter = priceFormatter;
    }

    @Override
    protected ShippingInfoBean getViewModelInstance() {
        return new ShippingInfoBean();
    }

    @Override
    public final ShippingInfoBean create(@Nullable final CartLike<?> data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final ShippingInfoBean model, final CartLike<?> data) {
        fillLabel(model, data);
        fillDescription(model, data);
        fillPrice(model, data);
    }

    protected void fillLabel(final ShippingInfoBean bean, @Nullable final CartLike<?> cartLike) {
        if (cartLike != null && cartLike.getShippingInfo() != null) {
            bean.setLabel(cartLike.getShippingInfo().getShippingMethodName());
        }
    }

    protected void fillDescription(final ShippingInfoBean bean, @Nullable final CartLike<?> cartLike) {
        if (cartLike != null && cartLike.getShippingInfo() != null) {
            final Reference<ShippingMethod> ref = cartLike.getShippingInfo().getShippingMethod();
            if (ref != null && ref.getObj() != null) {
                bean.setDescription(ref.getObj().getDescription());
            }
        }
    }

    protected void fillPrice(final ShippingInfoBean bean, @Nullable final CartLike<?> cartLike) {
        if (cartLike != null) {
            final MonetaryAmount amount = CartPriceUtils.calculateAppliedShippingPrice(cartLike)
                    .orElseGet(() -> zeroAmount(cartLike.getCurrency()));
            bean.setPrice(priceFormatter.format(amount));
        } else {
            bean.setPrice(priceFormatter.format(zeroAmount(currency)));
        }
    }

    private static MonetaryAmount zeroAmount(final CurrencyUnit currency) {
        return PriceUtils.zeroAmount(currency);
    }
}
