package com.commercetools.sunrise.common.sessions.customer;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.customers.Customer;

import javax.annotation.Nullable;
import javax.inject.Singleton;

@Singleton
public class UserInfoBeanFactory extends ViewModelFactory<UserInfoBean, Customer> {

    @Override
    protected UserInfoBean getViewModelInstance() {
        return new UserInfoBean();
    }

    @Override
    public final UserInfoBean create(@Nullable final Customer data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final UserInfoBean model, final Customer data) {
        fillLoggedIn(model, data);
        fillName(model, data);
        fillEmail(model, data);
    }

    protected void fillLoggedIn(final UserInfoBean bean, @Nullable final Customer customer) {
        bean.setLoggedIn(customer != null);
    }

    protected void fillName(final UserInfoBean bean, @Nullable final Customer customer) {
        if (customer != null) {
            bean.setName(customer.getFirstName());
        }
    }

    protected void fillEmail(final UserInfoBean bean, @Nullable final Customer customer) {
        if (customer != null) {
            bean.setEmail(customer.getEmail());
        }
    }
}
