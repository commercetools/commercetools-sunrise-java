package com.commercetools.sunrise.shoppingcart.carts;

import com.commercetools.sunrise.core.controllers.AbstractFormAction;
import com.commercetools.sunrise.models.carts.MyCartUpdater;
import play.data.FormFactory;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class DefaultRemoveFromCartFormAction extends AbstractFormAction<RemoveFromCartFormData> implements RemoveFromCartFormAction {

    private final RemoveFromCartFormData formData;
    private final MyCartUpdater myCartUpdater;

    @Inject
    DefaultRemoveFromCartFormAction(final FormFactory formFactory, final RemoveFromCartFormData formData,
                                    final MyCartUpdater myCartUpdater) {
        super(formFactory);
        this.formData = formData;
        this.myCartUpdater = myCartUpdater;
    }

    @Override
    protected Class<? extends RemoveFromCartFormData> formClass() {
        return formData.getClass();
    }

    @Override
    protected CompletionStage<?> onValidForm(final RemoveFromCartFormData formData) {
        return myCartUpdater.force(formData.removeLineItem());
    }
}
