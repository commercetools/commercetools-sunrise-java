package com.commercetools.sunrise.core.sessions;

import java.util.concurrent.CompletionStage;

public interface ResourceInCache<T> extends StoringOperations<T> {

    CompletionStage<T> get();

    void purge();
}
