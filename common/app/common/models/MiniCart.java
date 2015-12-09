package common.models;

import io.sphere.sdk.models.Base;

import static java.util.Collections.singletonList;

public class MiniCart extends Base {
    private Long totalItems;
    private String totalPrice;
    private MiniCartLineItems lineItems;

    public MiniCart() {
    }

    public MiniCart(final Long totalItems) {
        this.totalItems = totalItems;
        this.lineItems = new MiniCartLineItems(singletonList(new MiniCartLineItem())); // TODO implement
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
