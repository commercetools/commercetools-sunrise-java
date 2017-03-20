package com.commercetools.sunrise.framework.reverserouters.shoppingcart.cart;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultCartReverseRouter.class)
public interface CartReverseRouter extends SimpleCartReverseRouter, LocalizedReverseRouter {

    default Call cartDetailPageCall() {
        return cartDetailPageCall(locale().toLanguageTag());
    }

    default Call addLineItemProcessCall() {
        return addLineItemProcessCall(locale().toLanguageTag());
    }

    default Call changeLineItemQuantityProcessCall() {
        return changeLineItemQuantityProcessCall(locale().toLanguageTag());
    }

    default Call removeLineItemProcessCall() {
        return removeLineItemProcessCall(locale().toLanguageTag());
    }
}
