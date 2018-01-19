package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.models.carts.MyCart;
import com.commercetools.sunrise.models.customers.MyCustomer;
import play.mvc.Call;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.mvc.Results.redirect;

public class DefaultLogOutAction implements LogOutAction {

    private final MyCustomer myCustomer;
    private final MyCart myCart;

    @Inject
    protected DefaultLogOutAction(final MyCustomer myCustomer, final MyCart myCart) {
        this.myCustomer = myCustomer;
        this.myCart = myCart;
    }

    @Override
    public CompletionStage<Result> apply(final Supplier<Call> onSuccessCall) {
        myCustomer.remove();
        myCart.remove();
        return completedFuture(redirect(onSuccessCall.get()));
    }
}
