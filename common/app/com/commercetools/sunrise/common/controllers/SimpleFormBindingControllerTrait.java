package com.commercetools.sunrise.common.controllers;

import play.data.Form;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public interface SimpleFormBindingControllerTrait<T> extends FormBindingTrait<T> {
    CompletionStage<Result> handleValidForm(final Form<? extends T> form);
    CompletionStage<Result> handleInvalidForm(final Form<? extends T> form);
}
