package com.commercetools.sunrise.myaccount;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import javax.inject.Singleton;

@Singleton
public class UserInfoBeanFactory extends ViewModelFactory<UserInfoBean, UserInfoBeanFactory.Data> {

    public final UserInfoBean create(@Nullable final Customer customer) {
        final Data data = new Data(customer);
        return initializedViewModel(data);
    }

    @Override
    protected UserInfoBean getViewModelInstance() {
        return new UserInfoBean();
    }

    @Override
    protected final void initialize(final UserInfoBean bean, final Data data) {
        fillLoggedIn(bean, data);
        fillName(bean, data);
        fillEmail(bean, data);
    }

    protected void fillLoggedIn(final UserInfoBean bean, final Data data) {
        bean.setLoggedIn(data.customer != null);
    }

    protected void fillName(final UserInfoBean bean, final Data data) {
        if (data.customer != null) {
            bean.setName(data.customer.getFirstName());
        }
    }

    protected void fillEmail(final UserInfoBean bean, final Data data) {
        if (data.customer != null) {
            bean.setEmail(data.customer.getEmail());
        }
    }

    protected final static class Data extends Base {

        @Nullable
        public final Customer customer;

        public Data(@Nullable final Customer customer) {
            this.customer = customer;
        }
    }
}
