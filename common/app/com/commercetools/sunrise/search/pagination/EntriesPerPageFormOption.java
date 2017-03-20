package com.commercetools.sunrise.search.pagination;

import com.commercetools.sunrise.framework.viewmodels.forms.FormOption;

public interface EntriesPerPageFormOption extends FormOption<Integer> {

    static EntriesPerPageFormOption of(final String fieldLabel, final String fieldValue, final Integer value, final boolean isDefault) {
        return new EntriesPerPageFormOptionImpl(fieldLabel, fieldValue, value, isDefault);
    }
}
