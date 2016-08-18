package com.commercetools.sunrise.common.reverserouter;

import play.mvc.Call;

/**
 * Creates a {@link Call} like a reverse router.
 */
@FunctionalInterface
public interface ReverseCaller {
    Call call(Object ... args);
}
