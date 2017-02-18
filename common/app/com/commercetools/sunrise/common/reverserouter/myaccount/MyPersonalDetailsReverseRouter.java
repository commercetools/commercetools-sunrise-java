package com.commercetools.sunrise.common.reverserouter.myaccount;

import com.commercetools.sunrise.common.reverserouter.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionMyPersonalDetailsLocalizedReverseRouter.class)
public interface MyPersonalDetailsReverseRouter extends MyPersonalDetailsSimpleReverseRouter, LocalizedReverseRouter {

    default Call myPersonalDetailsPageCall() {
        return myPersonalDetailsPageCall(languageTag());
    }

    default Call myPersonalDetailsProcessFormCall() {
        return myPersonalDetailsProcessFormCall(languageTag());
    }
}
