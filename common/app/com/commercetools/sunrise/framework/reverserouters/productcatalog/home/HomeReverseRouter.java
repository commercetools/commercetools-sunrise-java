package com.commercetools.sunrise.framework.reverserouters.productcatalog.home;

import com.commercetools.sunrise.framework.reverserouters.ReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultHomeReverseRouter.class)
public interface HomeReverseRouter extends ReverseRouter {

    String HOME_PAGE = "homePageCall";

    Call homePageCall();
}
