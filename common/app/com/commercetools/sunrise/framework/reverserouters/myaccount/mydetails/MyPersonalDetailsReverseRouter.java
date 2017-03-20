package com.commercetools.sunrise.framework.reverserouters.myaccount.mydetails;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultMyPersonalDetailsReverseRouter.class)
public interface MyPersonalDetailsReverseRouter extends SimpleMyPersonalDetailsReverseRouter, LocalizedReverseRouter {

    default Call myPersonalDetailsPageCall() {
        return myPersonalDetailsPageCall(locale().toLanguageTag());
    }

    default Call myPersonalDetailsProcessCall() {
        return myPersonalDetailsProcessCall(locale().toLanguageTag());
    }
}
