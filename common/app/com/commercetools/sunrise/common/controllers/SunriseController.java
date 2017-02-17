package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.framework.ControllerComponentListSupplier;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Call;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseController extends Controller {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ComponentRegistry componentRegistry;

    protected SunriseController(final ComponentRegistry componentRegistry) {
        this.componentRegistry = componentRegistry;
    }

    public final Logger getLogger() {
        return logger;
    }

    public final ComponentRegistry getComponentRegistry() {
        return componentRegistry;
    }

    protected void register(final ControllerComponentListSupplier supplier) {
        componentRegistry.addAll(supplier.get());
    }

    protected void register(final ControllerComponent ... controllerComponents) {
        componentRegistry.addAll(asList(controllerComponents));
    }

    protected final CompletionStage<Result> redirectTo(final Call call) {
        return completedFuture(Results.redirect(call));
    }
}
