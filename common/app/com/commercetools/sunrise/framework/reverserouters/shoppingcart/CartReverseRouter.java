package com.commercetools.sunrise.framework.reverserouters.shoppingcart;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionCartLocalizedReverseRouter.class)
public interface CartReverseRouter extends CartSimpleReverseRouter, LocalizedReverseRouter {

    default Call cartDetailPageCall() {
        return cartDetailPageCall(languageTag());
    }

    default Call addLineItemProcessCall() {
        return addLineItemProcessCall(languageTag());
    }

    default Call changeLineItemQuantityProcessCall() {
        return changeLineItemQuantityProcessCall(languageTag());
    }

    default Call removeLineItemProcessCall() {
        return removeLineItemProcessCall(languageTag());
    }
}
