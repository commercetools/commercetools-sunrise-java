package com.commercetools.sunrise.common.reverserouter.productcatalog;

import com.commercetools.sunrise.common.reverserouter.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionHomeLocalizedReverseRouter.class)
public interface HomeReverseRouter extends HomeSimpleReverseRouter, LocalizedReverseRouter {

    default Call homePageCall() {
        return homePageCall(languageTag());
    }
}
