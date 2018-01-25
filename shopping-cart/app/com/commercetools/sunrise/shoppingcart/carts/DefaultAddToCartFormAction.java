package com.commercetools.sunrise.shoppingcart.carts;

import com.commercetools.sunrise.core.AbstractFormAction;
import com.commercetools.sunrise.models.carts.MyCartUpdater;
import io.sphere.sdk.carts.commands.updateactions.AddLineItem;
import play.data.FormFactory;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class DefaultAddToCartFormAction extends AbstractFormAction<AddToCartFormData> implements AddToCartFormAction {

    private final AddToCartFormData formData;
    private final MyCartUpdater myCartUpdater;

    @Inject
    DefaultAddToCartFormAction(final FormFactory formFactory, final AddToCartFormData formData,
                               final MyCartUpdater myCartUpdater) {
        super(formFactory);
        this.formData = formData;
        this.myCartUpdater = myCartUpdater;
    }

    @Override
    protected Class<? extends AddToCartFormData> formClass() {
        return formData.getClass();
    }

    @Override
    protected CompletionStage<?> onValidForm(final AddToCartFormData formData) {
        return myCartUpdater.applyOrCreate(AddLineItem.of(formData.lineItemDraft()));
    }
}
