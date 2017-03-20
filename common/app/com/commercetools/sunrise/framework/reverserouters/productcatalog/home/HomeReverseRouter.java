package com.commercetools.sunrise.framework.reverserouters.productcatalog.home;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultHomeReverseRouter.class)
public interface HomeReverseRouter extends SimpleHomeReverseRouter, LocalizedReverseRouter {

    default Call homePageCall() {
        return homePageCall(locale().toLanguageTag());
    }
}
