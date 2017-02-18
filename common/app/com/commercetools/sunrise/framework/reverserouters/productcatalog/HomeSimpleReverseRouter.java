package com.commercetools.sunrise.framework.reverserouters.productcatalog;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionHomeReverseRouter.class)
interface HomeSimpleReverseRouter {

    String HOME_PAGE = "homePage";

    Call homePageCall(final String languageTag);
}
