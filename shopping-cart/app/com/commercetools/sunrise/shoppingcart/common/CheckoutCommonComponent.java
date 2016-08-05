package com.commercetools.sunrise.shoppingcart.common;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.hooks.consumers.PageDataHook;
import com.commercetools.sunrise.framework.ControllerComponent;
import play.Configuration;

import javax.inject.Inject;

public class CheckoutCommonComponent implements ControllerComponent, PageDataHook {
    @Inject
    private Configuration configuration;

    @Override
    public void onPageDataCreated(final PageData pageData) {
        pageData.getHeader().setCustomerServiceNumber(configuration.getString("checkout.customerServiceNumber"));
    }
}
