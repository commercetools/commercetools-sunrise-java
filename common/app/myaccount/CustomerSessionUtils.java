package myaccount;

import io.sphere.sdk.customers.Customer;
import play.Logger;
import play.mvc.Http.Session;

import javax.annotation.Nullable;
import java.util.Optional;

public final class CustomerSessionUtils {

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
        return Optional.ofNullable(session.get(CustomerSessionKeys.CUSTOMER_ID));
    }

    public static Optional<String> getCustomerName(final Session session) {
        return Optional.ofNullable(session.get(CustomerSessionKeys.CUSTOMER_NAME));
    }

    public static Optional<String> getCustomerEmail(final Session session) {
        return Optional.ofNullable(session.get(CustomerSessionKeys.CUSTOMER_EMAIL));
    }

    public static void overwriteCustomerSessionData(@Nullable final Customer customer, final Session session) {
        if (customer != null) {
            final String id = customer.getId();
            final String name = customer.getName().getFirstName();
            final String email = customer.getEmail();
            session.put(CustomerSessionKeys.CUSTOMER_ID, id);
            session.put(CustomerSessionKeys.CUSTOMER_NAME, name);
            session.put(CustomerSessionKeys.CUSTOMER_EMAIL, email);
            Logger.debug("Saved customer in session: ID {}, name {}, email {}", id, name, email);
        } else {
            removeCustomer(session);
        }
    }

    public static void removeCustomer(final Session session) {
        session.remove(CustomerSessionKeys.CUSTOMER_ID);
        session.remove(CustomerSessionKeys.CUSTOMER_NAME);
        session.remove(CustomerSessionKeys.CUSTOMER_EMAIL);
        Logger.debug("Removed customer from session");
    }
}