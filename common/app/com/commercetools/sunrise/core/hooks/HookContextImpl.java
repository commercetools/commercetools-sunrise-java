package com.commercetools.sunrise.core.hooks;

import com.commercetools.sunrise.core.components.Component;
import com.commercetools.sunrise.core.injection.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.inject.Injector;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.sphere.sdk.utils.CompletableFutureUtils.successful;

@RequestScoped
final class HookContextImpl extends AbstractComponentRegistry implements HookContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(HookContext.class);

    private final Map<Class<? extends Component>, Component> initializedComponents = new HashMap<>();
    private final Injector injector;


    @Inject
    HookContextImpl(final GlobalComponentRegistry globalComponentRegistry, final Injector injector) {
        this.injector = injector;
        addAll(globalComponentRegistry.components());
    }

    @Override
    public <H extends Hook> void runEventHook(final Class<H> hookClass, final Consumer<H> f) {
        LOGGER.debug("Running EventHook {}", hookClass.getSimpleName());
        components().stream()
                .filter(hookClass::isAssignableFrom)
                .peek(hook -> LOGGER.debug("Executing {} triggered by {}", hook.getSimpleName(), hookClass.getSimpleName()))
                .map(this::getComponent)
                .forEach(hook -> f.accept((H) hook));
    }

    @Override
    public <H extends Hook, R> CompletionStage<R> runActionHook(final Class<H> hookClass, final BiFunction<H, R, CompletionStage<R>> f, final R param) {
        LOGGER.debug("Running ActionHook {}", hookClass.getSimpleName());
        CompletionStage<R> result = successful(param);
        final List<H> applicableHooks = components().stream()
                .filter(hookClass::isAssignableFrom)
                .map(this::getComponent)
                .map(x -> (H) x)
                .collect(Collectors.toList());
        for (final H hook : applicableHooks) {
            LOGGER.debug("Executing {} triggered by {}", hook.getClass().getSimpleName(), hookClass.getSimpleName());
            result = result.thenComposeAsync(res -> f.apply(hook, res), HttpExecution.defaultContext());
        }
        return result;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    private Component getComponent(final Class<? extends Component> componentClass) {
        Component component = initializedComponents.get(componentClass);
        if (component == null) {
            component = injector.instanceOf(componentClass);
            initializedComponents.put(componentClass, component);
            LOGGER.debug("Initialized component {}", componentClass.getSimpleName());
        }
        return component;
    }
}
