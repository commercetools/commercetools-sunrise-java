package com.commercetools.sunrise.shoppingcart.discountcodes;

import com.commercetools.sunrise.core.AbstractFormAction;
import com.commercetools.sunrise.models.carts.MyCartUpdater;
import com.google.inject.Inject;
import play.data.FormFactory;

import java.util.concurrent.CompletionStage;

final class DefaultRemoveDiscountCodeFormAction extends AbstractFormAction<RemoveDiscountCodeFormData> implements RemoveDiscountCodeFormAction {

    private final RemoveDiscountCodeFormData formData;
    private final MyCartUpdater myCartUpdater;

    @Inject
    DefaultRemoveDiscountCodeFormAction(final FormFactory formFactory, final RemoveDiscountCodeFormData formData,
                                        final MyCartUpdater myCartUpdater) {
        super(formFactory);
        this.formData = formData;
        this.myCartUpdater = myCartUpdater;
    }

    @Override
    protected Class<? extends RemoveDiscountCodeFormData> formClass() {
        return formData.getClass();
    }

    @Override
    protected CompletionStage<?> onValidForm(final RemoveDiscountCodeFormData formData) {
        return myCartUpdater.force(formData.removeDiscountCode());
    }
}
