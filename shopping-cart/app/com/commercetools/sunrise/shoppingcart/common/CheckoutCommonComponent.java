package com.commercetools.sunrise.shoppingcart.common;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.framework.ControllerComponent;
import play.Configuration;

import javax.inject.Inject;

public class CheckoutCommonComponent implements ControllerComponent, PageDataReadyHook {
    @Inject
    private Configuration configuration;

    @Override
    public void onPageDataReady(final PageData pageData) {
        pageData.getHeader().setCustomerServiceNumber(configuration.getString("checkout.customerServiceNumber"));
    }
}
