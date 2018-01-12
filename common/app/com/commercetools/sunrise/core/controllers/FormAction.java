package com.commercetools.sunrise.core.controllers;

import play.data.Form;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.function.Supplier;

public interface FormAction<F> {

    CompletionStage<Result> apply(Supplier<Result> onSuccess, Function<Form<? extends F>, CompletionStage<Result>> onBadRequest);
}
