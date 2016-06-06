package shoppingcart.checkout;

import common.controllers.SunrisePageData;
import common.hooks.SunrisePageDataHook;
import framework.ControllerSunriseComponent;
import play.Configuration;

import javax.inject.Inject;

public class CheckoutCommonComponent implements ControllerSunriseComponent, SunrisePageDataHook {
    @Inject
    private Configuration configuration;

    @Override
    public void acceptSunrisePageData(final SunrisePageData sunrisePageData) {
        sunrisePageData.getHeader().setCustomerServiceNumber(configuration.getString("checkout.customerServiceNumber"));
    }
}
