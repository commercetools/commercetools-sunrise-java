package com.commercetools.sunrise.common.reverserouter;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionMyPersonalDetailsReverseRouter.class)
public interface MyPersonalDetailsReverseRouter {

    Call myPersonalDetailsPageCall(final String languageTag);

    Call myPersonalDetailsProcessFormCall(final String languageTag);
}
