package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionMyPersonalDetailsLocalizedReverseRouter.class)
public interface MyPersonalDetailsReverseRouter extends MyPersonalDetailsSimpleReverseRouter, LocalizedReverseRouter {

    default Call myPersonalDetailsPageCall() {
        return myPersonalDetailsPageCall(languageTag());
    }

    default Call myPersonalDetailsProcessCall() {
        return myPersonalDetailsProcessCall(languageTag());
    }
}
