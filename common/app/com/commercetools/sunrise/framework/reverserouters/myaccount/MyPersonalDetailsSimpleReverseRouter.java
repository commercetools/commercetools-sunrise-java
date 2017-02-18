package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionMyPersonalDetailsReverseRouter.class)
interface MyPersonalDetailsSimpleReverseRouter {

    String MY_PERSONAL_DETAILS_PAGE = "myPersonalDetailsPageCall";

    Call myPersonalDetailsPageCall(final String languageTag);

    String MY_PERSONAL_DETAILS_PROCESS = "myPersonalDetailsProcessCall";

    Call myPersonalDetailsProcessCall(final String languageTag);
}
