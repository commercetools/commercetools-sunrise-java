package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultChangeAddressControllerAction.class)
public interface ChangeAddressControllerAction extends ControllerAction<ChangeAddressFormData> {

}
