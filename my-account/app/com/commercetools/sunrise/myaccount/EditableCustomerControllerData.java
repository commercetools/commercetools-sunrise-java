package com.commercetools.sunrise.myaccount;

import com.commercetools.sunrise.common.models.EditableResourceControllerData;
import io.sphere.sdk.customers.Customer;
import play.data.Form;

import javax.annotation.Nullable;

public abstract class EditableCustomerControllerData<F> extends EditableResourceControllerData<F, Customer> {

    protected EditableCustomerControllerData(final Form<? extends F> form, final Customer resource, @Nullable final Customer updatedResource) {
        super(form, resource, updatedResource);
    }

    protected Customer getCustomer() {
        return super.getResource();
    }
}
