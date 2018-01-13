package com.commercetools.sunrise.wishlist;

import com.commercetools.sunrise.core.AbstractFormAction;
import com.commercetools.sunrise.models.shoppinglists.MyWishlistUpdater;
import play.data.FormFactory;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class DefaultRemoveFromWishlistFormAction extends AbstractFormAction<RemoveFromWishlistFormData> implements RemoveFromWishlistFormAction {

    private final RemoveFromWishlistFormData formData;
    private final MyWishlistUpdater myWishlistUpdater;

    @Inject
    DefaultRemoveFromWishlistFormAction(final FormFactory formFactory, final RemoveFromWishlistFormData formData,
                                        final MyWishlistUpdater myWishlistUpdater) {
        super(formFactory);
        this.formData = formData;
        this.myWishlistUpdater = myWishlistUpdater;
    }

    @Override
    protected Class<? extends RemoveFromWishlistFormData> formClass() {
        return formData.getClass();
    }

    @Override
    protected CompletionStage<?> onValidForm(final RemoveFromWishlistFormData formData) {
        return myWishlistUpdater.force(formData.removeLineItem());
    }
}
