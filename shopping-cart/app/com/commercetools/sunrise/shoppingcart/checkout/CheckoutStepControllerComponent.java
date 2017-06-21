package com.commercetools.sunrise.shoppingcart.checkout;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.commercetools.sunrise.shoppingcart.checkout.address.viewmodels.CheckoutAddressPageContent;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.viewmodels.CheckoutConfirmationPageContent;
import com.commercetools.sunrise.shoppingcart.checkout.payment.viewmodels.CheckoutPaymentPageContent;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.viewmodels.CheckoutShippingPageContent;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

import static com.commercetools.sunrise.shoppingcart.checkout.CheckoutStep.*;

@Singleton
public class CheckoutStepControllerComponent implements ControllerComponent, PageDataReadyHook {

    private static final Map<Class<?>, CheckoutStep> MAPPING = initializeMapping();

    @Override
    public void onPageDataReady(final PageData pageData) {
        if (pageData.getContent() instanceof AbstractCheckoutPageContent) {
            final AbstractCheckoutPageContent content = (AbstractCheckoutPageContent) pageData.getContent();
            MAPPING.entrySet().stream()
                    .filter(entry -> entry.getKey().isAssignableFrom(content.getClass()))
                    .findAny()
                    .ifPresent(entry -> content.setStepWidget(entry.getValue()));
        }
    }

    protected static Map<Class<?>, CheckoutStep> initializeMapping() {
        final HashMap<Class<?>, CheckoutStep> result = new HashMap<>();
        result.put(CheckoutAddressPageContent.class, ADDRESS);
        result.put(CheckoutShippingPageContent.class, SHIPPING);
        result.put(CheckoutPaymentPageContent.class, PAYMENT);
        result.put(CheckoutConfirmationPageContent.class, CONFIRMATION);
        return result;
    }
}
