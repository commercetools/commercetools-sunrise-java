package com.commercetools.sunrise.common.reverserouter;

import play.mvc.Call;

public interface CheckoutReverseRouter {

    Call showCheckoutShippingForm(final String languageTag);
}
