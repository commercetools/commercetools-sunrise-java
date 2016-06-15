package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.hooks.Hook;
import com.commercetools.sunrise.hooks.RequestHook;
import com.commercetools.sunrise.common.pages.*;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
import com.google.inject.Injector;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.utils.FutureUtils;
import play.libs.concurrent.HttpExecution;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public abstract class SunriseFrameworkController extends Controller {
    @Inject
    private SphereClient sphere;
    @Inject
    private UserContext userContext;
    @Inject
    private TemplateEngine templateEngine;
    @Inject
    private PageMetaFactory pageMetaFactory;

    private final List<ControllerComponent> controllerComponents = new LinkedList<>();

    protected SunriseFrameworkController() {
    }

    public abstract Set<String> getFrameworkTags();

    @Inject
    public void setMultiControllerComponents(final MultiControllerComponentResolver multiComponent, final Injector injector) {
        final List<Class<? extends ControllerComponent>> components = multiComponent.findMatchingComponents(this);
        components.forEach(clazz -> {
            final ControllerComponent instance = injector.getInstance(clazz);
            controllerComponents.add(instance);
        });
    }

    protected final void registerControllerComponent(final ControllerComponent controllerComponent) {
        controllerComponents.add(controllerComponent);
    }

    public SphereClient sphere() {
        return sphere;
    }

    public UserContext userContext() {
        return userContext;
    }

    public TemplateEngine templateEngine() {
        return templateEngine;
    }

    @Nullable
    public static String getCsrfToken(final Http.Session session) {
        return session.get("csrfToken");
    }

    protected final SunrisePageData pageData(final PageContent content) {
        final SunrisePageData pageData = new SunrisePageData();
        pageData.setHeader(new PageHeader(content.getTitle()));
        pageData.setContent(content);
        pageData.setFooter(new PageFooter());
        pageData.setMeta(pageMetaFactory.create());
        return pageData;
    }

    protected final CompletionStage<Result> doRequest(final Supplier<CompletionStage<Result>> objectResultFunction) {
        return runAsyncHook(RequestHook.class, hook -> hook.onRequest(ctx()))
                .thenComposeAsync(unused -> objectResultFunction.get(), HttpExecution.defaultContext());
    }

    /**
     * Executes a hook which takes 0 to n parameters and returns nothing. The execution is synchronous and each
     * implementing component will be called after each other. This is normally used to achieve side effects.
     *
     * @param hookClass the class which represents the hook
     * @param consumer a computation that takes the hook instance as parameter which represents executing the hook
     * @param <T> the type of the hook
     */
    protected final <T extends Hook> void runVoidHook(final Class<T> hookClass, final Consumer<T> consumer) {
        controllerComponents.stream()
                .filter(x -> hookClass.isAssignableFrom(x.getClass()))
                .forEach(action -> consumer.accept((T) action));
    }

    /**
     * Executes a hook with one parameter that returns a value of the same type as the parameter. The execution is synchronous and each
     * implementing component will be called after each other. A typical use case is to use this as filter especially in combination with "withers".
     *
     * @param hookClass the class which represents the hook
     * @param f a computation (filter) that takes the hook and the parameter of type {@code R} as argument and returns a computed value of the type {@code R}
     * @param param the initial parameter for the filter
     * @param <T> the type of the hook
     * @param <R> the type of the parameter
     * @return the result of the filter chain, if there is no hooks then it will be the parameter itself, if there are multiple hooks then it will be applied like this: f<sub>3</sub>(f<sub>2</sub>(f<sub>1</sub>(initialParameter)))
     */
    protected final <T extends Hook, R> R runFilterHook(final Class<T> hookClass, final BiFunction<T, R, R> f, final R param) {
        R result = param;
        final List<T> applicableHooks = controllerComponents.stream()
                .filter(x -> hookClass.isAssignableFrom(x.getClass()))
                .map(x -> (T) x)
                .collect(Collectors.toList());
        for (final T hook : applicableHooks) {
            result = f.apply(hook, result);
        }
        return result;
    }

    /**
     * Executes a hook which takes 0 to n parameters and returns a {@link CompletionStage}.
     * The execution (just the creation of the {@link CompletionStage}) is synchronous and each implementing component will be called after each other and does not wait for the {@link CompletionStage} to be completed.
     * The underlying computation to complete the {@link CompletionStage} can be asynchronous and should run in parallel for the components.
     * The result should be completed at some point and a successful completion can also contain the value {@code null} hence the successful result is not used directly by the framework.
     * Before the hook {@link com.commercetools.sunrise.hooks.SunrisePageDataHook} is called, all asynchronous computations for the requests need to be completed successfully.
     * @param hookClass the class which represents the hook
     * @param f a possible asynchronous computation using the hook
     * @param <T> the type of the hook
     * @return a {@link CompletionStage} which is completed successfully if all underlying components completed successfully with this hook, otherwise a exceptionally completed {@link CompletionStage}
     */
    protected final <T extends Hook> CompletionStage<Object> runAsyncHook(final Class<T> hookClass, final Function<T, CompletionStage<?>> f) {
        //TODO throw a helpful NPE if component returns null instead of CompletionStage
        final List<CompletionStage<Void>> collect = controllerComponents.stream()
                .filter(x -> hookClass.isAssignableFrom(x.getClass()))
                .map(hook -> f.apply((T) hook))
                .map(stage -> (CompletionStage<Void>) stage)
                .collect(toList());
        final CompletionStage<?> listCompletionStage = FutureUtils.listOfFuturesToFutureOfList(collect);
        return listCompletionStage.thenApply(z -> null);
    }
}
