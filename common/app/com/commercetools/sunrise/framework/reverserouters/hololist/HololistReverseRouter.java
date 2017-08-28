package com.commercetools.sunrise.framework.reverserouters.hololist;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultHololistReverseRouter.class)
public interface HololistReverseRouter extends SimpleHololistReverseRouter, LocalizedReverseRouter {

    default Call addToHololistProcess() {
        return addToHololistProcessCall(locale().toLanguageTag());
    }

    default Call removeFromHololistProcess() {
        return removeFromHololistProcessCall(locale().toLanguageTag());
    }

    default Call clearHololistProcess() {
        return clearHololistProcessCall(locale().toLanguageTag());
    }

    default Call hololistPageCall() {
        return hololistPageCall(locale().toLanguageTag());
    }
}
