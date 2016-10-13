package com.commercetools.sunrise.myaccount;

import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;

public class UserInfoBeanFactory extends Base {

    public UserInfoBean create(@Nullable final Customer customer) {
        final UserInfoBean bean = new UserInfoBean();
        initialize(bean, customer);
        return bean;
    }

    protected final void initialize(final UserInfoBean bean, @Nullable final Customer customer) {
        fillLoggedIn(bean, customer);
        fillName(bean, customer);
        fillEmail(bean, customer);
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
