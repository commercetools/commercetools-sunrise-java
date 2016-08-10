package com.commercetools.sunrise.myaccount;

import com.commercetools.sunrise.common.sessions.SessionHandlerBase;
import com.google.inject.Injector;
import io.sphere.sdk.customers.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;

public class CustomerSessionHandler extends SessionHandlerBase<Customer> {

    private static final Logger logger = LoggerFactory.getLogger(CustomerSessionHandler.class);
    private static final String CUSTOMER_ID_SESSION_KEY = "sunrise-customer-id";
    private static final String CUSTOMER_EMAIL_SESSION_KEY = "sunrise-customer-email";
    private static final String USER_INFO_SESSION_KEY = "sunrise-user-info";

    @Inject
    private Injector injector;

    public Optional<String> findCustomerId(final Http.Session session) {
        return findValue(session, getCustomerIdSessionKey());
    }

    public Optional<String> findCustomerEmail(final Http.Session session) {
        return findValue(session, getCustomerEmailSessionKey());
    }

    public Optional<UserInfoBean> findUserInfo(final Http.Session session) {
        return findValue(session, getUserInfoSessionKey(), UserInfoBean.class);
    }

    @Override
    protected void overwriteRelatedValuesInSession(final Http.Session session, final Customer customer) {
        overwriteValue(session, getCustomerIdSessionKey(), customer.getId());
        overwriteValue(session, getCustomerEmailSessionKey(), customer.getEmail());
        overwriteValue(session, getUserInfoSessionKey(), injector.getInstance(UserInfoBeanFactory.class).create(customer));
    }

    @Override
    protected Set<String> sessionKeys() {
        return new HashSet<>(asList(getCustomerIdSessionKey(), getCustomerEmailSessionKey(), getUserInfoSessionKey()));
    }

    @Override
    protected Logger logger() {
        return logger;
    }

    protected String getCustomerIdSessionKey() {
        return CUSTOMER_ID_SESSION_KEY;
    }

    protected String getCustomerEmailSessionKey() {
        return CUSTOMER_EMAIL_SESSION_KEY;
    }

    protected String getUserInfoSessionKey() {
        return USER_INFO_SESSION_KEY;
    }
}
