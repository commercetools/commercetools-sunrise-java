package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.shoppingcart.CartBean;

public class OrderBean extends CartBean {

    private String orderNumber;
    private String orderDate;

    public OrderBean() {
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(final String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(final String orderDate) {
        this.orderDate = orderDate;
    }
}
