package com.commercetools.sunrise.common.reverserouter;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionCartReverseRouter.class)
public interface CartLocalizedReverseRouter extends CartReverseRouter, LocalizedReverseRouter {

    default Call showCart() {
        return showCart(languageTag());
    }

    default Call processAddProductToCartForm() {
        return processAddProductToCartForm(languageTag());
    }

    default Call processDeleteLineItemForm() {
        return processDeleteLineItemForm(languageTag());
    }

    default Call processChangeLineItemQuantityForm() {
        return processChangeLineItemQuantityForm(languageTag());
    }
}
