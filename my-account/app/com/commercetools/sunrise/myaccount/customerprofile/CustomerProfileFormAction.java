package com.commercetools.sunrise.myaccount.customerprofile;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultCustomerProfileFormAction.class)
@FunctionalInterface
public interface CustomerProfileFormAction extends FormAction<CustomerProfileFormData> {

}
