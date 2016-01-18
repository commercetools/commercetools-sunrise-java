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
    private MiniCartLineItems lineItems;

    public MiniCart() {
        this.totalItems = 0L;
        this.lineItems = new MiniCartLineItems();
    }

    public MiniCart(final Cart cart, final UserContext userContext, final ReverseRouter reverseRouter) {
        final MoneyContext moneyContext = MoneyContext.of(cart.getCurrency(), userContext.locale());
        this.totalItems = cart.getLineItems().stream().mapToLong(LineItem::getQuantity).sum();
        this.totalPrice = moneyContext.formatOrZero(calculateTotalPrice(cart));
        this.lineItems = new MiniCartLineItems(cart, userContext, reverseRouter);
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

    public MiniCartLineItems getLineItems() {
        return lineItems;
    }

    public void setLineItems(final MiniCartLineItems lineItems) {
        this.lineItems = lineItems;
    }
}
