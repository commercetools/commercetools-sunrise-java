package com.commercetools.sunrise.shoppingcart.common;

import com.commercetools.sunrise.common.pages.SunrisePageData;
import com.commercetools.sunrise.hooks.SunrisePageDataHook;
import com.commercetools.sunrise.framework.ControllerComponent;
import play.Configuration;

import javax.inject.Inject;

public class CheckoutCommonComponent implements ControllerComponent, SunrisePageDataHook {
    @Inject
    private Configuration configuration;

    @Override
    public void acceptSunrisePageData(final SunrisePageData sunrisePageData) {
        sunrisePageData.getHeader().setCustomerServiceNumber(configuration.getString("checkout.customerServiceNumber"));
    }
}
