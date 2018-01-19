package com.commercetools.sunrise.core.hooks;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@ImplementedBy(HookContextImpl.class)
public interface HookRunner {

    /**
     * Executes a hook which takes 0 to n parameters and returns a {@link CompletionStage}.
     * The execution (just the creation of the {@link CompletionStage}) is synchronous and each implementing component
     * will be called after each other and does not wait for the {@link CompletionStage} to be completed.
     * The underlying computation to complete the {@link CompletionStage} can be asynchronous and should run in parallel for the components.
     * The result should be completed at some point and a successful completion can also contain the value {@code null}
     * hence the successful result is not used directly by the framework.
     *
     * @param hookClass the class which represents the hook
     * @param consumer         a possible asynchronous computation using the hook
     * @param <H>       the type of the hook
     */
    <H extends ConsumerHook> void run(Class<H> hookClass, Consumer<H> consumer);

    /**
     * Executes a hook with one parameter that returns a value of the same type as the parameter, wrapped in a {@link CompletionStage}.
     * The execution is asynchronous and each implementing component will be called after each other.
     *
     * A typical use case is to use this as filter especially in combination with "withers".
     *
     * @param <H> the type of the hook
     * @param <R> the type of the parameter
     * @param hookClass the class which represents the hook
     * @param identity the initial parameter for the filter
     * @param asyncAccumulator a computation (filter) that takes the hook and the parameter of type {@code R} as argument and returns a computed value of the type {@code R}
     * @return the result of the filter chain, if there is no hooks then it will be the parameter itself, if there are multiple hooks then it will be applied like this: f<sub>3</sub>(f<sub>2</sub>(f<sub>1</sub>(initialParameter)))
     */
    <H extends ReducerHook, R> CompletionStage<R> run(Class<H> hookClass, R identity, BiFunction<R, H, CompletionStage<R>> asyncAccumulator);

    <H extends FilterHook, I, O> CompletionStage<O> run(Class<H> hookClass, I input, Function<I, CompletionStage<O>> execution,
                                                        Function<H, BiFunction<I, Function<I, CompletionStage<O>>, CompletionStage<O>>> accumulator);
}