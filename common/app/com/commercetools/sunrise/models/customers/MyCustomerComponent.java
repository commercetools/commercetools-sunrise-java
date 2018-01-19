package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class MyCustomerComponent implements ControllerComponent, PageDataHook {

    private final MyCustomer myCustomer;

    @Inject
    MyCustomerComponent(final MyCustomer myCustomer) {
        this.myCustomer = myCustomer;
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        return myCustomer.get()
                .thenApply(customerOpt -> customerOpt
                        .map(customer -> pageData.put("customer", customer))
                        .orElse(pageData));
    }
}
