package com.commercetools.sunrise.framework.controllers;

import com.commercetools.sunrise.framework.components.CommonControllerComponentsSupplier;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Call;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@RegisteredComponents(CommonControllerComponentsSupplier.class)
public abstract class SunriseController extends Controller {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public final Logger getLogger() {
        return logger;
    }

    protected final CompletionStage<Result> redirectTo(final Call call) {
        return completedFuture(Results.redirect(call));
    }
}
