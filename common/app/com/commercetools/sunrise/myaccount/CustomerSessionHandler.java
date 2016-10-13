package com.commercetools.sunrise.myaccount;

import com.commercetools.sunrise.common.sessions.SessionHandlerBase;
import io.sphere.sdk.customers.Customer;
import play.Configuration;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

import static java.util.Arrays.asList;

@Singleton
public class CustomerSessionHandler extends SessionHandlerBase<Customer> {

    private static final String DEFAULT_CUSTOMER_ID_SESSION_KEY = "sunrise-customer-id";
    private static final String DEFAULT_CUSTOMER_EMAIL_SESSION_KEY = "sunrise-customer-email";
    private static final String DEFAULT_USER_INFO_SESSION_KEY = "sunrise-user-info";
    private final String customerIdSessionKey;
    private final String customerEmailSessionKey;
    private final String userInfoSessionKey;
    @Inject
    private UserInfoBeanFactory userInfoBeanFactory;

    @Inject
    public CustomerSessionHandler(final Configuration configuration) {
        this.customerIdSessionKey = configuration.getString("session.customer.customerId", DEFAULT_CUSTOMER_ID_SESSION_KEY);
        this.customerEmailSessionKey = configuration.getString("session.customer.customerEmail", DEFAULT_CUSTOMER_EMAIL_SESSION_KEY);
        this.userInfoSessionKey = configuration.getString("session.customer.userInfo", DEFAULT_USER_INFO_SESSION_KEY);
    }

    public Optional<String> findCustomerId(final Http.Session session) {
        return findValueByKey(session, customerIdSessionKey);
    }

    public Optional<String> findCustomerEmail(final Http.Session session) {
        return findValueByKey(session, customerEmailSessionKey);
    }

    public Optional<UserInfoBean> findUserInfo(final Http.Session session) {
        return findValueByKey(session, userInfoSessionKey, UserInfoBean.class);
    }

    @Override
    protected void overwriteRelatedValuesInSession(final Http.Session session, final Customer customer) {
        overwriteStringValueByKey(session, customerIdSessionKey, customer.getId());
        overwriteStringValueByKey(session, customerEmailSessionKey, customer.getEmail());
        overwriteValueByKey(session, userInfoSessionKey, userInfoBeanFactory.create(customer));
    }

    @Override
    protected void removeRelatedValuesFromSession(final Http.Session session) {
        removeValuesByKey(session, asList(customerIdSessionKey, customerEmailSessionKey, userInfoSessionKey));
    }
}
