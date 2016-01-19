package common.models;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.utils.MoneyContext;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Base;

import static common.utils.PriceUtils.calculateTotalPrice;

public class MiniCart extends Base {
    private Long totalItems;
    private String totalPrice;
    private MiniCartLineItemsBean lineItems;

    public MiniCart() {
        this.totalItems = 0L;
        this.lineItems = new MiniCartLineItemsBean();
    }

    public MiniCart(final Cart cart, final UserContext userContext, final ReverseRouter reverseRouter) {
        final MoneyContext moneyContext = MoneyContext.of(cart.getCurrency(), userContext.locale());
        this.totalItems = cart.getLineItems().stream().mapToLong(LineItem::getQuantity).sum();
        this.totalPrice = moneyContext.formatOrZero(calculateTotalPrice(cart));
        this.lineItems = new MiniCartLineItemsBean(cart, userContext, reverseRouter);
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(final Long totalItems) {
        this.totalItems = totalItems;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(final String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public MiniCartLineItemsBean getLineItems() {
        return lineItems;
    }

    public void setLineItems(final MiniCartLineItemsBean lineItems) {
        this.lineItems = lineItems;
    }
}
