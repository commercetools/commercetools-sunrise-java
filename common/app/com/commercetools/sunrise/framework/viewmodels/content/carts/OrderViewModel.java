package com.commercetools.sunrise.framework.viewmodels.content.carts;

public class OrderViewModel extends CartViewModel {

    private String orderNumber;
    private String orderDate;

    public OrderViewModel() {
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
