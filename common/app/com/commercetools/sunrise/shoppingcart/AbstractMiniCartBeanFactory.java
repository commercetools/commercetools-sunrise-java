package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.products.PriceUtils;

import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.stream.Collectors;

import static com.commercetools.sunrise.common.utils.CartPriceUtils.calculateTotalPrice;

public abstract class AbstractMiniCartBeanFactory<T extends MiniCartBean, D extends CartLike<?>> extends ViewModelFactory<T, D> {

    private final CurrencyUnit currency;
    private final PriceFormatter priceFormatter;

    protected AbstractMiniCartBeanFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter) {
        this.currency = currency;
        this.priceFormatter = priceFormatter;
    }

    @Override
    protected void initialize(final T model, final D data) {
        fillTotalPrice(model, data);
        fillTotalItems(model, data);
        fillLineItems(model, data);
    }

    protected void fillTotalItems(final T bean, @Nullable final D cartLike) {
        final long totalItems;
        if (cartLike != null) {
            totalItems = cartLike.getLineItems().stream()
                    .mapToLong(LineItem::getQuantity)
                    .sum();
        } else {
            totalItems = 0;
        }
        bean.setTotalItems(totalItems);
    }

    protected void fillLineItems(final T bean, @Nullable final D cartLike) {
        if (cartLike != null) {
            bean.setLineItems(createLineItemList(cartLike));
        }
    }

    protected void fillTotalPrice(final T bean, @Nullable final D cartLike) {
        final MonetaryAmount totalPrice;
        if (cartLike != null) {
            totalPrice = calculateTotalPrice(cartLike);
        } else {
            totalPrice = zeroAmount(currency);
        }
        bean.setTotalPrice(priceFormatter.format(totalPrice));
    }

    private LineItemListBean createLineItemList(final CartLike<?> cartLike) {
        final LineItemListBean lineItemListBean = new LineItemListBean();
        lineItemListBean.setList(cartLike.getLineItems().stream()
                .map(this::createLineItem)
                .collect(Collectors.toList()));
        return lineItemListBean;
    }

    abstract LineItemBean createLineItem(final LineItem lineItem);

    protected final MonetaryAmount zeroAmount(final CurrencyUnit currency) {
        return PriceUtils.zeroAmount(currency);
    }
}
