package com.commercetools.sunrise.core.components;

import com.commercetools.sunrise.core.hooks.ComponentRegistry;
import play.inject.Injector;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.concurrent.CompletionStage;

/**
 * Action that registers the provided {@link ControllerComponent} list.
 */
final class ComponentsDisabler extends Action<DisableComponents> {

    private final Injector injector;

    @Inject
    ComponentsDisabler(final Injector injector) {
        this.injector = injector;
    }

    @Override
    public CompletionStage<Result> call(final Http.Context ctx) {
        if (configuration.value().length > 0) {
            // On creation of this action there isn't any HTTP context, necessary to initialize the ComponentRegistry
            final ComponentRegistry componentRegistry = injector.instanceOf(ComponentRegistry.class);
            Arrays.stream(configuration.value()).forEach(componentRegistry::remove);
        }
        return delegate.call(ctx);
    }
}
