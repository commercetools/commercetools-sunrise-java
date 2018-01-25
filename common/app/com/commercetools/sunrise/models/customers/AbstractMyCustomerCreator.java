package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.AbstractHookRunner;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.carts.MyCart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class AbstractMyCustomerCreator extends AbstractHookRunner<CustomerSignInResult, CustomerCreateCommand> implements MyCustomerCreator {

    private final SphereClient sphereClient;
    private final MyCustomer myCustomer;
    private final MyCart myCart;

    protected AbstractMyCustomerCreator(final HookRunner hookRunner, final SphereClient sphereClient,
                                        final MyCustomer myCustomer, final MyCart myCart) {
        super(hookRunner);
        this.sphereClient = sphereClient;
        this.myCustomer = myCustomer;
        this.myCart = myCart;
    }

    @Override
    public CompletionStage<CustomerSignInResult> get(final CustomerDraft customerDraft) {
        return runHook(buildRequest(customerDraft), r -> {
            final CompletionStage<CustomerSignInResult> resultStage = sphereClient.execute(r);
            resultStage.thenAcceptAsync(result -> {
                myCustomer.store(result.getCustomer());
                myCart.store(result.getCart());
            }, HttpExecution.defaultContext());
            return resultStage;
        });
    }

    @Override
    protected CompletionStage<CustomerSignInResult> runHook(final CustomerCreateCommand request,
                                                            final Function<CustomerCreateCommand, CompletionStage<CustomerSignInResult>> execution) {
        return hookRunner().run(MyCustomerCreatorHook.class, request, execution, h -> h::on);
    }

    protected abstract CustomerCreateCommand buildRequest(CustomerDraft customerDraft);
}
