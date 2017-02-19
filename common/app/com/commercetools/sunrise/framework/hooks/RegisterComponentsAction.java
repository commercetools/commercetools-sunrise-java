package com.commercetools.sunrise.framework.hooks;

import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.components.ControllerComponentsSupplier;
import com.google.inject.Injector;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.concurrent.CompletionStage;

/**
 * Action that registers the provided {@link ControllerComponent} list.
 */
final class RegisterComponentsAction extends Action<RegisteredComponents> {

    private final Injector injector;

    @Inject
    RegisterComponentsAction(final Injector injector) {
        this.injector = injector;
    }

    @Override
    public CompletionStage<Result> call(final Http.Context ctx) {
        final ComponentRegistry componentRegistry = injector.getInstance(ComponentRegistry.class);
        Arrays.stream(configuration.value())
                .forEach(componentClass -> registerComponent(componentRegistry, componentClass));
        return delegate.call(ctx);
    }

    private void registerComponent(final ComponentRegistry componentRegistry, final Class<? extends ControllerComponentsSupplier> componentClass) {
        final ControllerComponentsSupplier controllerComponent = injector.getInstance(componentClass);
        componentRegistry.addAll(controllerComponent.get());
    }
}
