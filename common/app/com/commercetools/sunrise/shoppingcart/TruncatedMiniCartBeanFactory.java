package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.Cart;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.money.CurrencyUnit;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class TruncatedMiniCartBeanFactory extends MiniCartBeanFactory {

    @Nullable
    private Integer lineItemsLimit;

    @Inject
    public TruncatedMiniCartBeanFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter, final LineItemBeanFactory lineItemBeanFactory) {
        super(currency, priceFormatter, lineItemBeanFactory);
    }

    public final MiniCartBean create(@Nullable final Cart cart, final int lineItemsLimit) {
        this.lineItemsLimit = lineItemsLimit;
        return super.create(cart);
    }

    @Override
    protected void fillLineItems(final MiniCartBean bean, final Data<Cart> data) {
        super.fillLineItems(bean, data);
        if (lineItemsLimit != null) {
            bean.getLineItems().setList(truncateLineItems(bean.getLineItems().getList(), lineItemsLimit));
        }
    }

    /**
     * Limits the mini cart line item entries to avoid a too large mini cart that won't fit in session.
     * @param lineItemList the line items in cart
     * @param limit the maximum amount of line items to return
     * @return the line items in cart limited up to the given amount
     */
    private List<LineItemBean> truncateLineItems(final List<LineItemBean> lineItemList, final int limit) {
        return lineItemList.stream()
                .limit(limit)
                .collect(toList());
    }
}
