package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import io.sphere.sdk.models.Base;

public class CustomerIdOrderNumberPair extends Base {

    private String customerId;
    private String orderNumber;

    public CustomerIdOrderNumberPair(final String customerId, final String orderNumber) {
        this.customerId = customerId;
        this.orderNumber = orderNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }
}
