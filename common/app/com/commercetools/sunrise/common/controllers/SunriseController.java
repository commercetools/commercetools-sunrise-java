package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.ctp.MetricAction;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.hooks.events.RequestStartedHook;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Call;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import javax.annotation.Nullable;
import javax.inject.Named;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseController extends Controller {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final RequestHookContext hookContext;

    protected SunriseController(final RequestHookContext hookContext) {
        this.hookContext = hookContext;
    }

    public abstract Set<String> getFrameworkTags();

    public Logger getLogger() {
        return logger;
    }

    protected final void registerControllerComponent(final ControllerComponent controllerComponent) {
        hookContext.add(controllerComponent);
    }

    protected CompletionStage<Result> doRequest(final Supplier<CompletionStage<Result>> nextAction) {
        RequestStartedHook.runHook(hookContext, ctx());
        return nextAction.get()
                .thenApplyAsync(res -> {
                    MetricAction.logRequestData(ctx());
                    return res;
                }, HttpExecution.defaultContext());
    }

    protected final CompletionStage<Result> redirectTo(final Call call) {
        return completedFuture(Results.redirect(call));
    }

    @Inject(optional = true)
    private void setMultiControllerComponents(@Nullable @Named("controllers") final MultiControllerComponentResolver controllersMultiComponent, final MultiControllerComponentResolver multiComponent, final Injector injector) {
        if (controllersMultiComponent != null) {
            addMultiComponents(controllersMultiComponent, injector);
        }
        if (multiComponent != null) {
            addMultiComponents(multiComponent, injector);
        }
    }

    private void addMultiComponents(final MultiControllerComponentResolver multiComponent, final Injector injector) {
        final List<Class<? extends ControllerComponent>> components = multiComponent.findMatchingComponents(this);
        components.forEach(clazz -> {
            final ControllerComponent instance = injector.getInstance(clazz);
            hookContext.add(instance);
        });
    }
}
