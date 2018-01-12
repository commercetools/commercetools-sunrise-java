package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.core.controllers.AbstractFormAction;
import com.commercetools.sunrise.models.customers.MyCustomerUpdater;
import io.sphere.sdk.customers.commands.updateactions.RemoveAddress;
import play.data.FormFactory;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultRemoveAddressFormAction extends AbstractFormAction<RemoveAddressFormData> implements RemoveAddressFormAction {

    private final RemoveAddressFormData formData;
    private final MyCustomerUpdater myCustomerUpdater;

    @Inject
    protected DefaultRemoveAddressFormAction(final FormFactory formFactory, final RemoveAddressFormData formData,
                                             final MyCustomerUpdater myCustomerUpdater) {
        super(formFactory);
        this.formData = formData;
        this.myCustomerUpdater = myCustomerUpdater;
    }

    @Override
    protected Class<? extends RemoveAddressFormData> formClass() {
        return formData.getClass();
    }

    @Override
    protected CompletionStage<?> onValidForm(final RemoveAddressFormData formData) {
        return myCustomerUpdater.force(RemoveAddress.of(formData.addressId()));
    }
}
