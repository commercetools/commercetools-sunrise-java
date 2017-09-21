package com.commercetools.sunrise.framework.components.controllers;

import com.commercetools.sunrise.framework.hooks.ComponentRegistry;
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
final class ComponentsRegisterer extends Action<RegisteredComponents> {

    private final Injector injector;

    @Inject
    ComponentsRegisterer(final Injector injector) {
        this.injector = injector;
    }

    @Override
    public CompletionStage<Result> call(final Http.Context ctx) {
        if (configuration.value().length > 0) {
            // On creation of this action there isn't any HTTP context, necessary to initialize the ComponentRegistry
            final ComponentRegistry componentRegistry = injector.instanceOf(ComponentRegistry.class);
            Arrays.stream(configuration.value())
                    .forEach(componentClass -> registerComponent(componentRegistry, componentClass));
        }
        return delegate.call(ctx);
    }

    private void registerComponent(final ComponentRegistry componentRegistry, final Class<? extends ControllerComponentSupplier> componentClass) {
        final ControllerComponentSupplier controllerComponent = injector.instanceOf(componentClass);
        componentRegistry.addAll(controllerComponent.get());
    }
}
