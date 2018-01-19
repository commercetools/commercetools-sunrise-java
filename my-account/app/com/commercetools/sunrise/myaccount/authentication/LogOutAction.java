package com.commercetools.sunrise.myaccount.authentication;

import com.google.inject.ImplementedBy;
import play.mvc.Call;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

@ImplementedBy(DefaultLogOutAction.class)
@FunctionalInterface
public interface LogOutAction {

    CompletionStage<Result> apply(Supplier<Call> onSuccessCall);
}
