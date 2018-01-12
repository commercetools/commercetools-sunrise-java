package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.models.carts.MyCartInSession;
import com.commercetools.sunrise.models.customers.MyCustomerInSession;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class DefaultLogOutControllerAction implements LogOutControllerAction {

    private final MyCustomerInSession myCustomerInSession;
    private final MyCartInSession myCartInSession;

    @Inject
    protected DefaultLogOutControllerAction(final MyCustomerInSession myCustomerInSession, final MyCartInSession myCartInSession) {
        this.myCustomerInSession = myCustomerInSession;
        this.myCartInSession = myCartInSession;
    }

    @Override
    public CompletionStage<Result> apply(final Supplier<Result> onSuccess) {
        myCustomerInSession.remove();
        myCartInSession.remove();
        return completedFuture(onSuccess.get());
    }
}
