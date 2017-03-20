package com.commercetools.sunrise.myaccount.myorders.myorderlist.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

public class OrderOverviewViewModel extends ViewModel {

    private String orderNumber;
    private String orderDate;
    private String total;
    private String paymentStatus;
    private String shipping;
    private String showOrderUrl;

    public OrderOverviewViewModel() {
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

    public String getTotal() {
        return total;
    }

    public void setTotal(final String total) {
        this.total = total;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(final String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(final String shipping) {
        this.shipping = shipping;
    }

    public String getShowOrderUrl() {
        return showOrderUrl;
    }

    public void setShowOrderUrl(final String showOrderUrl) {
        this.showOrderUrl = showOrderUrl;
    }
}
