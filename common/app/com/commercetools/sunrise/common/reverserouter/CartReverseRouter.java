package com.commercetools.sunrise.common.reverserouter;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionCartReverseRouter.class)
public interface CartReverseRouter {
    Call showCart(final String languageTag);

    Call processAddProductToCartForm(final String languageTag);

    Call processDeleteLineItemForm(final String languageTag);

    Call processChangeLineItemQuantityForm(final String languageTag);
}
