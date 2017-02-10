package com.commercetools.sunrise.common.reverserouter;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionHomeLocalizedReverseRouter.class)
public interface HomeLocalizedReverseRouter extends HomeReverseRouter, LocalizedReverseRouter {

    default Call homePageCall() {
        return homePageCall(languageTag());
    }
}
