package com.commercetools.sunrise.framework.reverserouters.productcatalog.home;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(SimpleHomeReverseRouterByReflection.class)
public interface SimpleHomeReverseRouter {

    String HOME_PAGE = "homePageCall";

    Call homePageCall(final String languageTag);
}
