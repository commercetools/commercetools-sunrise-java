package com.commercetools.sunrise.shoppingcart.common;

import com.commercetools.sunrise.common.models.ControllerData;
import play.data.Form;

import javax.annotation.Nullable;

public abstract class EditableResourceControllerData<F, R> extends ControllerData {

    private final Form<? extends F> form;
    private final R resource;

    protected EditableResourceControllerData(final Form<? extends F> form, final R resource, @Nullable final R updatedResource) {
        this.form = form;
        this.resource = updatedResource != null ? updatedResource : resource;
    }

    protected Form<? extends F> getForm() {
        return form;
    }

    protected R getResource() {
        return resource;
    }
}
