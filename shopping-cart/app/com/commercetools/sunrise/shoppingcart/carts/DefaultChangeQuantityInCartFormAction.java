package com.commercetools.sunrise.shoppingcart.carts;

import com.commercetools.sunrise.core.controllers.AbstractFormAction;
import com.commercetools.sunrise.models.carts.MyCartUpdater;
import play.data.FormFactory;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class DefaultChangeQuantityInCartFormAction extends AbstractFormAction<ChangeQuantityInCartFormData> implements ChangeQuantityInCartFormAction {

    private final ChangeQuantityInCartFormData formData;
    private final MyCartUpdater myCartUpdater;

    @Inject
    DefaultChangeQuantityInCartFormAction(final FormFactory formFactory, final ChangeQuantityInCartFormData formData,
                                          final MyCartUpdater myCartUpdater) {
        super(formFactory);
        this.formData = formData;
        this.myCartUpdater = myCartUpdater;
    }

    @Override
    protected Class<? extends ChangeQuantityInCartFormData> formClass() {
        return formData.getClass();
    }

    @Override
    protected CompletionStage<?> onValidForm(final ChangeQuantityInCartFormData formData) {
        return myCartUpdater.force(formData.changeLineItemQuantity());
    }
}
