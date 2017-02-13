package com.commercetools.sunrise.hooks;

import com.google.inject.ImplementedBy;

@ImplementedBy(RequestHookContextImpl.class)
public interface RequestHookContext extends HookContext, RequestHookRunner {
}
