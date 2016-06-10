package com.commercetools.sunrise.wedecidelatercommon;

import play.mvc.Call;

public interface CheckoutReverseRouter {

    Call showCheckoutShippingForm(final String languageTag);
}
