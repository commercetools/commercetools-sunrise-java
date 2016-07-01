package com.commercetools.sunrise.common.reverserouter;

import play.mvc.Call;

public interface CartReverseRouter {
    Call showCart(final String languageTag);

    Call processAddProductToCartForm(final String languageTag);

    Call processDeleteLineItemForm(final String languageTag);

    Call processChangeLineItemQuantityForm(final String languageTag);
}
