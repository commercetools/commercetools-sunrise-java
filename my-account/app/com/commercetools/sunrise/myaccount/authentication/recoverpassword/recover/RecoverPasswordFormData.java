package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import com.google.inject.ImplementedBy;

/**
 * The data required to send a reset password email to a customer.
 */
@ImplementedBy(DefaultRecoverPasswordFormData.class)
public interface RecoverPasswordFormData {

    /**
     * The customer email address for which a password reset email should be send.
     *
     * @return the customer email.
     */
    String email();
}
