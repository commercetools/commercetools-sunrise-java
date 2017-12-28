package com.commercetools.sunrise.core.viewmodels.header;

import com.commercetools.sunrise.core.viewmodels.ViewModel;
import io.sphere.sdk.carts.Cart;

public class PageHeader extends ViewModel {

    private String title;
    private String customerServiceNumber;
    private Cart miniCart;

    public PageHeader() {
    }

    public PageHeader(final String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Cart getMiniCart() {
        return miniCart;
    }

    public void setMiniCart(final Cart miniCart) {
        this.miniCart = miniCart;
    }

    public String getCustomerServiceNumber() {
        return customerServiceNumber;
    }

    public void setCustomerServiceNumber(final String customerServiceNumber) {
        this.customerServiceNumber = customerServiceNumber;
    }
}
