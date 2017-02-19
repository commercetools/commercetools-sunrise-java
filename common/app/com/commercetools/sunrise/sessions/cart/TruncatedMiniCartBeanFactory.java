package com.commercetools.sunrise.sessions.cart;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import com.commercetools.sunrise.common.models.carts.LineItemBean;
import com.commercetools.sunrise.common.models.carts.LineItemBeanFactory;
import com.commercetools.sunrise.common.models.carts.MiniCartBean;
import com.commercetools.sunrise.common.models.carts.MiniCartBeanFactory;
import io.sphere.sdk.carts.Cart;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.money.CurrencyUnit;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class TruncatedMiniCartBeanFactory extends MiniCartBeanFactory {

    private static final int LINE_ITEMS_LIMIT = 5;

    @Inject
    public TruncatedMiniCartBeanFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter, final LineItemBeanFactory lineItemBeanFactory) {
        super(currency, priceFormatter, lineItemBeanFactory);
    }

    @Override
    protected void fillLineItems(final MiniCartBean bean, @Nullable final Cart cart) {
        super.fillLineItems(bean, cart);
        if (bean.getLineItems() != null) {
            bean.getLineItems().setList(truncateLineItems(bean.getLineItems().getList(), LINE_ITEMS_LIMIT));
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
