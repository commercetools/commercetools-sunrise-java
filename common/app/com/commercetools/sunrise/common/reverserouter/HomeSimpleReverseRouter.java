package com.commercetools.sunrise.common.reverserouter;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionHomeReverseRouter.class)
interface HomeSimpleReverseRouter {

    Call homePageCall(final String languageTag);

}
