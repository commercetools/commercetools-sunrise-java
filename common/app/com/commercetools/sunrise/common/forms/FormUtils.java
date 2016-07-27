package com.commercetools.sunrise.common.forms;

import play.data.Form;

import javax.annotation.Nullable;

public final class FormUtils {

    private FormUtils() {
    }

    @Nullable
    public static String extractFormField(@Nullable final Form<?> form, final String fieldName) {
        return form != null ? form.field(fieldName).value() : null;
    }

}
