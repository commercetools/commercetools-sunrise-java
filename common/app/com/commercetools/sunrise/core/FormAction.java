package com.commercetools.sunrise.core;

import play.data.Form;
import play.mvc.Call;
import play.mvc.Result;
import play.twirl.api.Content;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.function.Supplier;

public interface FormAction<F> {

    CompletionStage<Result> apply(Supplier<Call> onSuccessCall, Function<Form<? extends F>, CompletionStage<Content>> onBadRequest);
}
