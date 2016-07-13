package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.localization.LocalizationSelectorBean;
import com.commercetools.sunrise.common.models.ModelBean;
import com.commercetools.sunrise.shoppingcart.MiniCartBean;

public class PageHeader extends ModelBean {

    private String title;
    private String customerServiceNumber;
    private LocalizationSelectorBean location;
    private MiniCartBean miniCart;
    private PageNavMenu navMenu;

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

    public LocalizationSelectorBean getLocation() {
        return location;
    }

    public void setLocation(final LocalizationSelectorBean location) {
        this.location = location;
    }

    public MiniCartBean getMiniCart() {
        return miniCart;
    }

    public void setMiniCart(final MiniCartBean miniCart) {
        this.miniCart = miniCart;
    }

    public PageNavMenu getNavMenu() {
        return navMenu;
    }

    public void setNavMenu(final PageNavMenu navMenu) {
        this.navMenu = navMenu;
    }

    public String getCustomerServiceNumber() {
        return customerServiceNumber;
    }

    public void setCustomerServiceNumber(final String customerServiceNumber) {
        this.customerServiceNumber = customerServiceNumber;
    }
}
