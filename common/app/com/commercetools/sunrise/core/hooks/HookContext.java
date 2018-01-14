package com.commercetools.sunrise.core.hooks;

import com.google.inject.ImplementedBy;

@ImplementedBy(HookContextImpl.class)
public interface HookContext extends HookRunner, ComponentRegistry {

}
