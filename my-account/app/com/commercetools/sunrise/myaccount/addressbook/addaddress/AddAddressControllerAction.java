package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultAddAddressControllerAction.class)
public interface AddAddressControllerAction extends ControllerAction<AddAddressFormData> {

}
