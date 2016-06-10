package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.models.LocationSelector;
import com.commercetools.sunrise.common.models.NavMenuData;
import shoppingcart.MiniCartBean;
import io.sphere.sdk.models.Base;

public class PageHeader extends Base {
    private String title;
    private String customerServiceNumber;
    private LocationSelector location;
    private MiniCartBean miniCart;
    private NavMenuData navMenu;

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

    public LocationSelector getLocation() {
        return location;
    }

    public void setLocation(final LocationSelector location) {
        this.location = location;
    }

    public MiniCartBean getMiniCart() {
        return miniCart;
    }

    public void setMiniCart(final MiniCartBean miniCart) {
        this.miniCart = miniCart;
    }

    public NavMenuData getNavMenu() {
        return navMenu;
    }

    public void setNavMenu(final NavMenuData navMenu) {
        this.navMenu = navMenu;
    }

    public String getCustomerServiceNumber() {
        return customerServiceNumber;
    }

    public void setCustomerServiceNumber(final String customerServiceNumber) {
        this.customerServiceNumber = customerServiceNumber;
    }
}
