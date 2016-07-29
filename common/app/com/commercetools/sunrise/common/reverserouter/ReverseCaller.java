package com.commercetools.sunrise.common.reverserouter;

import play.mvc.Call;

public interface ReverseCaller {
    Call call(Object... args);
}
