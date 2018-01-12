package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.sessions.ResourceInSession;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Keeps some parts from the customer in session, such as customer ID, email and some general info.
 */
@ImplementedBy(MyCustomerInSessionImpl.class)
public interface MyCustomerInSession extends ResourceInSession<Customer> {

    @Override
    Optional<String> findId();

    @Override
    Optional<Long> findVersion();

    Optional<String> findEmail();

    Optional<String> findCustomerGroupId();

    @Override
    void store(@Nullable final Customer customer);

    @Override
    void remove();
}
