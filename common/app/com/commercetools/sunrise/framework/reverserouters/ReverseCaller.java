package com.commercetools.sunrise.framework.reverserouters;

import play.mvc.Call;

/**
 * Creates a {@link Call} like a reverse router.
 */
@FunctionalInterface
public interface ReverseCaller {

    Call call(Object ... args);
}
