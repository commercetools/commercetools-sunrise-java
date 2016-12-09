package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.AddressBeanFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartLike;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class TruncatedMiniCartBeanFactory extends MiniCartBeanFactory {

    @Nullable
    private Integer lineItemsLimit;

    @Inject
    public TruncatedMiniCartBeanFactory(final UserContext userContext, final PriceFormatter priceFormatter,
                                        final AddressBeanFactory addressBeanFactory,
                                        final LineItemBeanFactory lineItemBeanFactory) {
        super(userContext, priceFormatter, addressBeanFactory, lineItemBeanFactory);
    }

    public MiniCartBean create(@Nullable final Cart cart, final int lineItemsLimit) {
        this.lineItemsLimit = lineItemsLimit;
        return super.create(cart);
    }

    @Override
    protected List<LineItemBean> createLineItemList(final CartLike<?> cartLike) {
        final List<LineItemBean> lineItemList = super.createLineItemList(cartLike);
        return lineItemsLimit != null ? truncateLineItems(lineItemList, lineItemsLimit) : lineItemList;
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
