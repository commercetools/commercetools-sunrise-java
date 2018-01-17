package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.models.carts.MyCartInCache;
import com.commercetools.sunrise.models.customers.MyCustomerInCache;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class DefaultLogOutAction implements LogOutAction {

    private final MyCustomerInCache myCustomerInCache;
    private final MyCartInCache myCartInCache;

    @Inject
    protected DefaultLogOutAction(final MyCustomerInCache myCustomerInCache, final MyCartInCache myCartInCache) {
        this.myCustomerInCache = myCustomerInCache;
        this.myCartInCache = myCartInCache;
    }

    @Override
    public CompletionStage<Result> apply(final Supplier<Result> onSuccess) {
        myCustomerInCache.remove();
        myCartInCache.remove();
        return completedFuture(onSuccess.get());
    }
}
