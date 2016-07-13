package com.commercetools.sunrise.myaccount;

import io.sphere.sdk.customers.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http.Session;

import javax.annotation.Nullable;
import java.util.Optional;

public final class CustomerSessionUtils {
    private static final Logger logger = LoggerFactory.getLogger(CustomerSessionUtils.class);
    public static final String CUSTOMER_ID_SESSION_KEY = "customer-id";
    public static final String CUSTOMER_NAME_SESSION_KEY = "customer-name";
    public static final String CUSTOMER_EMAIL_SESSION_KEY = "customer-email";

    private CustomerSessionUtils() {
    }

    public static UserBean getUserBean(final Session session) {
        final UserBean user = new UserBean();
        user.setLoggedIn(isLoggedIn(session));
        getCustomerName(session).ifPresent(user::setName);
        return user;
    }

    public static boolean isLoggedIn(final Session session) {
        return getCustomerId(session).isPresent();
    }

    public static Optional<String> getCustomerId(final Session session) {
        return Optional.ofNullable(session.get(CUSTOMER_ID_SESSION_KEY));
    }

    public static Optional<String> getCustomerName(final Session session) {
        return Optional.ofNullable(session.get(CUSTOMER_NAME_SESSION_KEY));
    }

    public static Optional<String> getCustomerEmail(final Session session) {
        return Optional.ofNullable(session.get(CUSTOMER_EMAIL_SESSION_KEY));
    }

    public static void overwriteCustomerSessionData(@Nullable final Customer customer, final Session session) {
        if (customer != null) {
            final String id = customer.getId();
            final String name = customer.getName().getFirstName();
            final String email = customer.getEmail();
            session.put(CUSTOMER_ID_SESSION_KEY, id);
            session.put(CUSTOMER_NAME_SESSION_KEY, name);
            session.put(CUSTOMER_EMAIL_SESSION_KEY, email);
            logger.debug("Saved customer in session: ID {}, name {}, email {}", id, name, email);
        } else {
            removeCustomerSessionData(session);
        }
    }

    public static void removeCustomerSessionData(final Session session) {
        session.remove(CUSTOMER_ID_SESSION_KEY);
        session.remove(CUSTOMER_NAME_SESSION_KEY);
        session.remove(CUSTOMER_EMAIL_SESSION_KEY);
        logger.debug("Removed customer from session");
    }
}