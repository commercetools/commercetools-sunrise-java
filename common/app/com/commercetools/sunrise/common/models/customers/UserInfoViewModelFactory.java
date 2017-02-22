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
    public final UserInfoViewModel create(@Nullable final Customer customer) {
        return super.create(customer);
    }

    @Override
    protected final void initialize(final UserInfoViewModel viewModel, final Customer customer) {
        fillLoggedIn(viewModel, customer);
        fillName(viewModel, customer);
        fillEmail(viewModel, customer);
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
