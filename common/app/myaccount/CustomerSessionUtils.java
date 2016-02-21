package myaccount;

import io.sphere.sdk.customers.Customer;
import play.Logger;
import play.mvc.Http.Session;

import javax.annotation.Nullable;

public final class CustomerSessionUtils {
    private CustomerSessionUtils() {
    }

    public static UserBean getUser(final Session session) {
        final UserBean user = new UserBean();
        user.setLoggedIn(isLoggedIn(session));
        user.setName(getCustomerName(session));
        return user;
    }

    public static boolean isLoggedIn(final Session session) {
        return session.get(CustomerSessionKeys.CUSTOMER_ID) != null;
    }

    @Nullable
    public static String getCustomerName(final Session session) {
        return session.get(CustomerSessionKeys.CUSTOMER_NAME);
    }

    public static void overwriteCustomerSessionData(@Nullable final Customer customer, final Session session) {
        if (customer != null) {
            final String id = customer.getId();
            final String name = customer.getName().getFirstName();
            session.put(CustomerSessionKeys.CUSTOMER_ID, id);
            session.put(CustomerSessionKeys.CUSTOMER_NAME, name);
            Logger.debug("Saved customer in session: ID \"{}\", name \"{}\"", id , name);
        } else {
            removeCustomer(session);
        }
    }

    public static void removeCustomer(final Session session) {
        session.remove(CustomerSessionKeys.CUSTOMER_ID);
        session.remove(CustomerSessionKeys.CUSTOMER_NAME);
        Logger.debug("Removed customer from session");
    }
}