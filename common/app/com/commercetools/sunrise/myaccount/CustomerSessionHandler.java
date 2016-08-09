package com.commercetools.sunrise.myaccount;

import io.sphere.sdk.customers.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import javax.annotation.Nullable;
import java.util.Optional;

public class CustomerSessionHandler extends DefaultSessionHandler<Customer> {

    private static final Logger logger = LoggerFactory.getLogger(CustomerSessionHandler.class);
    private static final String ID_SESSION_KEY = "customer-id";
    private static final String EMAIL_SESSION_KEY = "customer-email";
    private static final String NAME_SESSION_KEY = "customer-name";

    public Optional<String> findCustomerId(final Http.Session session) {
        return findValue(session, getIdSessionKey());
    }

    public Optional<String> findCustomerEmail(final Http.Session session) {
        return findValue(session, getEmailSessionKey());
    }

    public Optional<String> findCustomerName(final Http.Session session) {
        return findValue(session, getNameSessionKey());
    }

    @Override
    public void overwriteSession(final Http.Session session, @Nullable final Customer customer) {
        if (customer != null) {
            overwriteValue(session, getIdSessionKey(), customer.getId());
            overwriteValue(session, getEmailSessionKey(), customer.getEmail());
            overwriteValue(session, getNameSessionKey(), customer.getFirstName());
            logger.debug("Saved customer {} in session", customer.getId());
        } else {
            removeFromSession(session);
        }
    }

    @Override
    public void removeFromSession(final Http.Session session) {
        removeValue(session, getIdSessionKey());
        removeValue(session, getEmailSessionKey());
        removeValue(session, getNameSessionKey());
        logger.debug("Removed customer from session");
    }

    protected String getIdSessionKey() {
        return ID_SESSION_KEY;
    }

    protected String getNameSessionKey() {
        return NAME_SESSION_KEY;
    }

    protected String getEmailSessionKey() {
        return EMAIL_SESSION_KEY;
    }
}
