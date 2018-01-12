package com.commercetools.sunrise.core.controllers;

import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

public interface ControllerAction {

    CompletionStage<Result> apply(Supplier<Result> onSuccess);
}
