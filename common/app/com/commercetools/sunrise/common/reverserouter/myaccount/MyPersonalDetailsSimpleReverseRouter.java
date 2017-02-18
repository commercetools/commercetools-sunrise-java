package com.commercetools.sunrise.common.reverserouter.myaccount;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionMyPersonalDetailsReverseRouter.class)
interface MyPersonalDetailsSimpleReverseRouter {

    Call myPersonalDetailsPageCall(final String languageTag);

    Call myPersonalDetailsProcessFormCall(final String languageTag);
}
