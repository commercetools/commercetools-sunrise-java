package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.core.FormAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultRemoveAddressFormAction.class)
@FunctionalInterface
public interface RemoveAddressFormAction extends FormAction<RemoveAddressFormData> {

}
