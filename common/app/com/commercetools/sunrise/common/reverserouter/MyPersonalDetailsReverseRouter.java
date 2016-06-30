package com.commercetools.sunrise.common.reverserouter;

import play.mvc.Call;

public interface MyPersonalDetailsReverseRouter {

    Call myPersonalDetailsPageCall(final String languageTag);

    Call myPersonalDetailsProcessFormCall(final String languageTag);
}
