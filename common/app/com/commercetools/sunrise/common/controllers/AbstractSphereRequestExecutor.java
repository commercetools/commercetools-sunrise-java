package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.hooks.HookContext;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Base;

public abstract class AbstractSphereRequestExecutor extends Base {

    private final SphereClient sphereClient;
    private final HookContext hookContext;

    protected AbstractSphereRequestExecutor(final SphereClient sphereClient, final HookContext hookContext) {
        this.sphereClient = sphereClient;
        this.hookContext = hookContext;
    }

    protected final SphereClient getSphereClient() {
        return sphereClient;
    }

    protected final HookContext getHookContext() {
        return hookContext;
    }
}
