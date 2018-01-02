package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.sessions.StoringStrategy;
import io.sphere.sdk.customers.Customer;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

/**
 * Keeps some parts from the customer in session, such as customer ID, email and some general info.
 */
@Singleton
final class CustomerInSessionImpl implements CustomerInSession {

    private final String cookieName;
    private final String emailSessionKey;
    private final String customerGroupSessionKey;
    private final boolean cookieSecure;
    private final boolean cookieHttpOnly;
    private final StoringStrategy storingStrategy;

    @Inject
    CustomerInSessionImpl(final Configuration globalConfig, final StoringStrategy storingStrategy) {
        final Configuration config = globalConfig.getConfig("sunrise.customers");
        this.cookieName = config.getString("cookieName");
        this.emailSessionKey = config.getString("emailSessionKey");
        this.customerGroupSessionKey = config.getString("customerGroupSessionKey");
        this.cookieSecure = config.getBoolean("cookieHttpOnly");
        this.cookieHttpOnly = config.getBoolean("cookieHttpOnly");
        this.storingStrategy = storingStrategy;
    }

    @Override
    public Optional<String> findCustomerId() {
        return storingStrategy.findInCookies(cookieName);
    }

    @Override
    public Optional<String> findCustomerEmail() {
        return storingStrategy.findInSession(emailSessionKey);
    }

    @Override
    public Optional<String> findCustomerGroupId() {
        return storingStrategy.findInSession(customerGroupSessionKey);
    }

    @Override
    public void store(@Nullable final Customer customer) {
        storeCustomerId(customer);
        storeEmail(customer);
        storeCustomerGroupId(customer);
    }

    private void storeCustomerId(final @Nullable Customer customer) {
        final String customerId = customer != null ? customer.getId() : null;
        storingStrategy.overwriteInCookies(cookieName, customerId, cookieHttpOnly, cookieSecure);
    }

    private void storeEmail(final @Nullable Customer customer) {
        final String email = customer != null ? customer.getEmail() : null;
        storingStrategy.overwriteInSession(emailSessionKey, email);
    }

    private void storeCustomerGroupId(final @Nullable Customer customer) {
        final String customerGroupId = customer != null && customer.getCustomerGroup() != null ? customer.getCustomerGroup().getId() : null;
        storingStrategy.overwriteInSession(customerGroupSessionKey, customerGroupId);
    }

    @Override
    public void remove() {
        storingStrategy.removeFromCookies(cookieName);
        storingStrategy.removeFromSession(emailSessionKey);
        storingStrategy.removeFromSession(customerGroupSessionKey);
    }
}
