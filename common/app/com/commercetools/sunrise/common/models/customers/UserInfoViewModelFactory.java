package com.commercetools.sunrise.common.models.customers;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.customers.Customer;

import javax.annotation.Nullable;
import javax.inject.Singleton;

@Singleton
public class UserInfoViewModelFactory extends ViewModelFactory<UserInfoViewModel, Customer> {

    @Override
    protected UserInfoViewModel getViewModelInstance() {
        return new UserInfoViewModel();
    }

    @Override
    public final UserInfoViewModel create(@Nullable final Customer data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final UserInfoViewModel model, final Customer data) {
        fillLoggedIn(model, data);
        fillName(model, data);
        fillEmail(model, data);
    }

    protected void fillLoggedIn(final UserInfoViewModel model, @Nullable final Customer customer) {
        model.setLoggedIn(customer != null);
    }

    protected void fillName(final UserInfoViewModel model, @Nullable final Customer customer) {
        if (customer != null) {
            model.setName(customer.getFirstName());
        }
    }

    protected void fillEmail(final UserInfoViewModel model, @Nullable final Customer customer) {
        if (customer != null) {
            model.setEmail(customer.getEmail());
        }
    }
}
