package com.commercetools.sunrise.wishlist;

import com.commercetools.sunrise.core.AbstractFormAction;
import com.commercetools.sunrise.models.shoppinglists.MyWishlistUpdater;
import io.sphere.sdk.shoppinglists.LineItemDraft;
import io.sphere.sdk.shoppinglists.commands.updateactions.AddLineItem;
import play.data.FormFactory;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultAddToWishlistFormAction extends AbstractFormAction<AddToWishlistFormData> implements AddToWishlistFormAction {

    private final AddToWishlistFormData formData;
    private final MyWishlistUpdater myWishlistUpdater;

    @Inject
    protected DefaultAddToWishlistFormAction(final FormFactory formFactory, final AddToWishlistFormData formData,
                                             final MyWishlistUpdater myWishlistUpdater) {
        super(formFactory);
        this.formData = formData;
        this.myWishlistUpdater = myWishlistUpdater;
    }

    @Override
    protected Class<? extends AddToWishlistFormData> formClass() {
        return formData.getClass();
    }

    @Override
    protected CompletionStage<?> onValidForm(final AddToWishlistFormData formData) {
        final LineItemDraft draft = formData.lineItemDraft();
        final AddLineItem addLineItem = AddLineItem.of(draft.getProductId())
                .withVariantId(draft.getVariantId())
                .withQuantity(draft.getQuantity())
                .withAddedAt(draft.getAddedAt())
                .withCustom(draft.getCustom());
        return myWishlistUpdater.applyOrCreate(addLineItem);
    }
}
