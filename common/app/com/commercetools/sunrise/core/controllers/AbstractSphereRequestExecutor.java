package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Base;

public abstract class AbstractSphereRequestExecutor extends Base {

    private final SphereClient sphereClient;
    private final HookRunner hookRunner;

    protected AbstractSphereRequestExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        this.sphereClient = sphereClient;
        this.hookRunner = hookRunner;
    }

    protected final SphereClient getSphereClient() {
        return sphereClient;
    }

    protected final HookRunner getHookRunner() {
        return hookRunner;
    }
}
