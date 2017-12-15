package com.commercetools.sunrise.models.search.pagination;

import com.commercetools.sunrise.core.viewmodels.forms.FormOption;

public interface EntriesPerPageFormOption extends FormOption<Integer> {

    static EntriesPerPageFormOption of(final String fieldLabel, final String fieldValue, final Integer value, final boolean isDefault) {
        return new EntriesPerPageFormOptionImpl(fieldLabel, fieldValue, value, isDefault);
    }
}
