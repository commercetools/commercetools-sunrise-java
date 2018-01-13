package com.commercetools.sunrise.core;

import java.util.concurrent.CompletionStage;

public interface ResourceCreator<T, D> {

    CompletionStage<T> get(D draft);
}
