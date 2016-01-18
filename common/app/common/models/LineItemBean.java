package common.models;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.utils.MoneyContext;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Base;

public class LineItemBean extends Base {
    private String lineItemId;
    private long quantity;
    private String totalPrice;
    private ProductVariantBean variant;

    public LineItemBean() {
    }

    public LineItemBean(final LineItem lineItem, final UserContext userContext, final ReverseRouter reverseRouter) {
        final MoneyContext moneyContext = MoneyContext.of(lineItem.getPrice().getValue().getCurrency(), userContext.locale());
        this.lineItemId = lineItem.getId();
        this.quantity = lineItem.getQuantity();
        this.totalPrice = moneyContext.formatOrZero(lineItem.getTotalPrice());
        this.variant = new ProductVariantBean(lineItem, userContext, reverseRouter);
    }

    public String getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(final String lineItemId) {
        this.lineItemId = lineItemId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(final String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ProductVariantBean getVariant() {
        return variant;
    }

    public void setVariant(final ProductVariantBean variant) {
        this.variant = variant;
    }
}
