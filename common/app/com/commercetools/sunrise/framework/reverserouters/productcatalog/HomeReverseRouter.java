package com.commercetools.sunrise.framework.reverserouters.productcatalog;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionHomeLocalizedReverseRouter.class)
public interface HomeReverseRouter extends HomeSimpleReverseRouter, LocalizedReverseRouter {

    default Call homePageCall() {
        return homePageCall(languageTag());
    }
}
