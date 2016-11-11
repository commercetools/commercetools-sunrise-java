package com.commercetools.sunrise.myaccount;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.sessions.DataFromResourceStoringOperations;
import com.commercetools.sunrise.common.sessions.ObjectStoringSessionStrategy;
import com.google.inject.Injector;
import io.sphere.sdk.customers.Customer;
import play.Configuration;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Keeps some parts from the customer in session, such as customer ID, email and some general info.
 */
@RequestScoped
public class CustomerInSession extends DataFromResourceStoringOperations<Customer> {

    private static final String DEFAULT_CUSTOMER_ID_SESSION_KEY = "sunrise-customer-id";
    private static final String DEFAULT_CUSTOMER_EMAIL_SESSION_KEY = "sunrise-customer-email";
    private static final String DEFAULT_USER_INFO_SESSION_KEY = "sunrise-user-info";
    private final String customerIdSessionKey;
    private final String customerEmailSessionKey;
    private final String userInfoSessionKey;
    private final ObjectStoringSessionStrategy session;
    @Inject
    private Injector injector;

    @Inject
    public CustomerInSession(final ObjectStoringSessionStrategy session, final Configuration configuration) {
        this.customerIdSessionKey = configuration.getString("session.customer.customerId", DEFAULT_CUSTOMER_ID_SESSION_KEY);
        this.customerEmailSessionKey = configuration.getString("session.customer.customerEmail", DEFAULT_CUSTOMER_EMAIL_SESSION_KEY);
        this.userInfoSessionKey = configuration.getString("session.customer.userInfo", DEFAULT_USER_INFO_SESSION_KEY);
        this.session = session;
    }

    public Optional<String> findCustomerId() {
        return session.findValueByKey(customerIdSessionKey);
    }

    public Optional<String> findCustomerEmail() {
        return session.findValueByKey(customerEmailSessionKey);
    }

    public Optional<UserInfoBean> findUserInfo() {
        return session.findObjectByKey(userInfoSessionKey, UserInfoBean.class);
    }

    @Override
    protected void storeAssociatedData(final Customer customer) {
        session.overwriteObjectByKey(userInfoSessionKey, createUserInfo(customer));
        session.overwriteValueByKey(customerIdSessionKey, customer.getId());
        session.overwriteValueByKey(customerEmailSessionKey, customer.getEmail());
    }

    @Override
    protected void removeAssociatedData() {
        session.removeObjectByKey(userInfoSessionKey);
        session.removeValueByKey(customerIdSessionKey);
        session.removeValueByKey(customerEmailSessionKey);
    }

    protected UserInfoBean createUserInfo(final Customer customer) {
        return injector.getInstance(UserInfoBeanFactory.class).create(customer);
    }
}
