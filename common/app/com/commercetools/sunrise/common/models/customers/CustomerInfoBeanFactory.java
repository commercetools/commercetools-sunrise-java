package com.commercetools.sunrise.common.models.customers;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.customers.Customer;

public class CustomerInfoBeanFactory extends ViewModelFactory<CustomerInfoBean, Customer> {

    @Override
    protected CustomerInfoBean getViewModelInstance() {
        return new CustomerInfoBean();
    }

    @Override
    public final CustomerInfoBean create(final Customer data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final CustomerInfoBean model, final Customer data) {
        fillCustomer(model, data);
    }

    protected void fillCustomer(final CustomerInfoBean model, final Customer customer) {
        model.setCustomer(customer);
    }
}
