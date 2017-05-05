package com.commercetools.sunrise.sessions.customer;

import com.commercetools.sunrise.framework.viewmodels.content.customers.UserInfoViewModel;
import com.commercetools.sunrise.sessions.ResourceStoringOperations;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Keeps some parts from the customer in session, such as customer ID, email and some general info.
 */
@ImplementedBy(DefaultCustomerInSession.class)
public interface CustomerInSession extends ResourceStoringOperations<Customer> {

    Optional<String> findCustomerId();

    Optional<String> findCustomerEmail();

    Optional<String> findCustomerGroupId();

    Optional<UserInfoViewModel> findUserInfo();

    @Override
    void store(@Nullable final Customer customer);

    @Override
    void remove();
}
