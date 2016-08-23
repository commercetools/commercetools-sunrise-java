package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.models.ViewModel;

public class MiniCartBean extends ViewModel {

    private Long totalItems;
    private String totalPrice;
    private LineItemListBean lineItems;

    public MiniCartBean() {
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

    public LineItemListBean getLineItems() {
        return lineItems;
    }

    public void setLineItems(final LineItemListBean lineItems) {
        this.lineItems = lineItems;
    }
}
