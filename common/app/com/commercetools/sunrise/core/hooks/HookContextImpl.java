package com.commercetools.sunrise.core.hooks;

import com.commercetools.sunrise.core.components.Component;
import com.commercetools.sunrise.core.injection.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.inject.Injector;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.sphere.sdk.utils.CompletableFutureUtils.successful;
import static java.util.stream.Collectors.toList;

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
    public <H extends ReducerHook, R> CompletionStage<R> run(final Class<H> hookClass, final R identity,
                                                             final BiFunction<R, H, CompletionStage<R>> asyncAccumulator) {
        LOGGER.debug("Running ReducerHook {}", hookClass.getSimpleName());
        CompletionStage<R> resultStage = successful(identity);
        final List<H> applicableHooks = components().stream()
                .filter(hookClass::isAssignableFrom)
                .map(this::getComponent)
                .peek(component -> LOGGER.debug("Executing {} triggered by {}", component.getClass().getSimpleName(), hookClass.getSimpleName()))
                .map(component -> (H) component)
                .collect(toList());
        for (final H hook : applicableHooks) {
            LOGGER.debug("Executing {} triggered by {}", hook.getClass().getSimpleName(), hookClass.getSimpleName());
            resultStage = resultStage.thenComposeAsync(res -> asyncAccumulator.apply(res, hook), HttpExecution.defaultContext());
        }
        return resultStage;
    }

    @Override
    public <H extends FilterHook, I, O> CompletionStage<O> run(final Class<H> hookClass, final I input,
                                                               final Function<I, CompletionStage<O>> execution,
                                                               final Function<H, BiFunction<I, Function<I, CompletionStage<O>>, CompletionStage<O>>> accumulator) {
        final List<H> applicableHooks = getComponents(hookClass);
        return runRecursive(applicableHooks.iterator(), input, execution, accumulator);
    }

    private <H extends FilterHook, I, O> CompletionStage<O> runRecursive(final Iterator<H> iterator, final I input,
                                                                         final Function<I, CompletionStage<O>> execution,
                                                                         final Function<H, BiFunction<I, Function<I, CompletionStage<O>>, CompletionStage<O>>> accumulator) {
        if (!iterator.hasNext()) {
            return execution.apply(input);
        } else {
            return accumulator.apply(iterator.next())
                    .apply(input, res -> runRecursive(iterator, res, execution, accumulator));
        }
    }


    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @SuppressWarnings("unchecked")
    private <H extends FilterHook> List<H> getComponents(final Class<H> hookClass) {
        return components().stream()
                .filter(hookClass::isAssignableFrom)
                .map(this::getComponent)
                .map(component -> (H) component)
                .collect(toList());
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
