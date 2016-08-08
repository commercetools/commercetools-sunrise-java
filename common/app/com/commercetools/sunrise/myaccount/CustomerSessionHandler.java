package com.commercetools.sunrise.myaccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import javax.annotation.Nullable;
import java.util.Optional;

public class CustomerSessionHandler extends DefaultSessionHandler<CustomerInfo> {

    private static final Logger logger = LoggerFactory.getLogger(CustomerSessionHandler.class);
    private static final String ID_SESSION_KEY = "customer-id";
    private static final String EMAIL_SESSION_KEY = "customer-email";
    private static final String NAME_SESSION_KEY = "customer-name";

    @Override
    public Optional<CustomerInfo> findInSession(final Http.Session session) {
        final Optional<String> customerId = getCustomerId(session);
        final Optional<String> customerEmail = getCustomerEmail(session);
        final Optional<String> customerName = getCustomerName(session);
        if (customerId.isPresent() && customerEmail.isPresent()) {
            final CustomerInfo customerInfo = CustomerInfo.of(customerId.get(), customerEmail.get(), customerName.orElse(null));
            return Optional.of(customerInfo);
        }
        return Optional.empty();
    }

    @Override
    public void overwriteSession(final Http.Session session, @Nullable final CustomerInfo customerInfo) {
        if (customerInfo != null) {
            overwriteValue(session, getIdSessionKey(), customerInfo.getId());
            overwriteValue(session, getEmailSessionKey(), customerInfo.getEmail());
            overwriteValue(session, getNameSessionKey(), customerInfo.getName().orElse(null));
            logger.debug("Saved customer in session: {}", customerInfo);
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

    protected Optional<String> getCustomerId(final Http.Session session) {
        return findValue(session, getIdSessionKey());
    }

    protected Optional<String> getCustomerEmail(final Http.Session session) {
        return findValue(session, getEmailSessionKey());
    }

    protected Optional<String> getCustomerName(final Http.Session session) {
        return findValue(session, getNameSessionKey());
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
