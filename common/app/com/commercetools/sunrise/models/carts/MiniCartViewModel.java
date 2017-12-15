package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.viewmodels.ViewModel;

public class MiniCartViewModel extends ViewModel {

    private Long totalItems;
    private String totalPrice;
    private LineItemListViewModel lineItems;

    public MiniCartViewModel() {
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

    public LineItemListViewModel getLineItems() {
        return lineItems;
    }

    public void setLineItems(final LineItemListViewModel lineItems) {
        this.lineItems = lineItems;
    }
}
