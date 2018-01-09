package com.commercetools.sunrise.core.controllers;

import java.util.concurrent.CompletionStage;

public interface ResourceCreator<T, D> {

    CompletionStage<T> get(D draft);
}
