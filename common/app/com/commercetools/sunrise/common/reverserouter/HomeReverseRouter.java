package com.commercetools.sunrise.common.reverserouter;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionHomeReverseRouter.class)
public interface HomeReverseRouter {

    Call homePageCall(final String languageTag);

}
