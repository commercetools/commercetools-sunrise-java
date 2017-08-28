package com.commercetools.sunrise.framework.reverserouters.hololist;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(SimpleHololistReverseRouterByReflection.class)
public interface SimpleHololistReverseRouter {
    String ADD_TO_HOLOLIST_PROCESS = "addToHololistProcessCall";

    String REMOVE_FROM_HOLOLIST_PROCESS = "removeFromHololistProcessCall";

    String CLEAR_HOLOLIST_PROCESS = "clearHololistProcessCall";

    String HOLOLIST_PAGE = "hololistPageCall";

    Call addToHololistProcessCall(String languageTag);

    Call removeFromHololistProcessCall(String languageTag);

    Call clearHololistProcessCall(String languageTag);

    Call hololistPageCall(String languageTag);
}
