package com.commercetools.sunrise.shoppingcart.common;

import com.commercetools.sunrise.common.pages.SunrisePageData;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.SunrisePageDataHook;
import com.commercetools.sunrise.shoppingcart.checkout.address.CheckoutAddressPageContent;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.CheckoutConfirmationPageContent;
import com.commercetools.sunrise.shoppingcart.checkout.payment.CheckoutPaymentPageContent;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.CheckoutShippingPageContent;

import java.util.HashMap;
import java.util.Map;

import static com.commercetools.sunrise.shoppingcart.common.StepWidgetBean.*;

public class CheckoutStepWidgetComponent implements ControllerComponent, SunrisePageDataHook {
    private static final Map<Class<?>, StepWidgetBean> mapping = setupData();

    private static HashMap setupData() {
        final HashMap<Class<?>, StepWidgetBean> result = new HashMap<>();
        result.put(CheckoutAddressPageContent.class, ADDRESS);
        result.put(CheckoutShippingPageContent.class, SHIPPING);
        result.put(CheckoutPaymentPageContent.class, PAYMENT);
        result.put(CheckoutConfirmationPageContent.class, CONFIRMATION);
        return result;
    }

    @Override
    public void acceptSunrisePageData(final SunrisePageData sunrisePageData) {
        if (sunrisePageData.getContent() instanceof CheckoutPageContent) {
            final CheckoutPageContent content = (CheckoutPageContent) sunrisePageData.getContent();
            final StepWidgetBean stepWidgetBean = mapping.entrySet().stream()
                    .filter(e -> e.getKey().isAssignableFrom(content.getClass()))
                    .findFirst()
                    .map(e -> e.getValue())
                    .orElse(null);
            content.setStepWidget(stepWidgetBean);
        }
    }
}
