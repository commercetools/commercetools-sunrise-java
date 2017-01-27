package com.commercetools.sunrise.shoppingcart.common;

import com.commercetools.sunrise.common.models.EditableResourceControllerData;
import io.sphere.sdk.carts.Cart;
import play.data.Form;

import javax.annotation.Nullable;

public abstract class EditableCartControllerData<F> extends EditableResourceControllerData<F, Cart> {

    protected EditableCartControllerData(final Form<? extends F> form, final Cart cart, @Nullable final Cart updatedCart) {
        super(form, cart, updatedCart);
    }

    protected Cart getCart() {
        return super.getResource();
    }
}
