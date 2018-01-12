package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.sessions.AbstractResourceInSession;
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
final class MyCustomerInSessionImpl extends AbstractResourceInSession<Customer> implements MyCustomerInSession {

    private final String emailSessionKey;
    private final String customerGroupSessionKey;

    @Inject
    MyCustomerInSessionImpl(final Configuration globalConfig, final StoringStrategy storingStrategy) {
        super(globalConfig.getConfig("sunrise.customers"), storingStrategy);
        this.emailSessionKey = getConfiguration().getString("emailSessionKey");
        this.customerGroupSessionKey = getConfiguration().getString("customerGroupSessionKey");
    }

    @Override
    public Optional<String> findEmail() {
        return getStoringStrategy().findInSession(emailSessionKey);
    }

    @Override
    public Optional<String> findCustomerGroupId() {
        return getStoringStrategy().findInSession(customerGroupSessionKey);
    }

    @Override
    public void store(@Nullable final Customer customer) {
        super.store(customer);
        storeEmail(customer);
        storeCustomerGroupId(customer);
    }

    private void storeEmail(final @Nullable Customer customer) {
        final String email = customer != null ? customer.getEmail() : null;
        getStoringStrategy().overwriteInSession(emailSessionKey, email);
    }

    private void storeCustomerGroupId(final @Nullable Customer customer) {
        final String customerGroupId = customer != null && customer.getCustomerGroup() != null ? customer.getCustomerGroup().getId() : null;
        getStoringStrategy().overwriteInSession(customerGroupSessionKey, customerGroupId);
    }

    @Override
    public void remove() {
        super.remove();
        getStoringStrategy().removeFromSession(emailSessionKey);
        getStoringStrategy().removeFromSession(customerGroupSessionKey);
    }
}
