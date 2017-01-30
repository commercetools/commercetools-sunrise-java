package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.myaccount.EditableCustomerControllerData;
import io.sphere.sdk.customers.Customer;
import play.data.Form;

import javax.annotation.Nullable;

public class MyPersonalDetailsControllerData extends EditableCustomerControllerData<MyPersonalDetailsFormData> {

    public MyPersonalDetailsControllerData(final Form<? extends MyPersonalDetailsFormData> form, final Customer customer, @Nullable final Customer updatedCustomer) {
        super(form, customer, updatedCustomer);
    }

    @Override
    public Form<? extends MyPersonalDetailsFormData> getForm() {
        return super.getForm();
    }

    @Override
    public Customer getCustomer() {
        return super.getCustomer();
    }
}
