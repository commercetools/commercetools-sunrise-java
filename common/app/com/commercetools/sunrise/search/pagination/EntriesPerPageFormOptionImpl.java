package com.commercetools.sunrise.search.pagination;

import com.commercetools.sunrise.framework.viewmodels.forms.AbstractFormOption;

class EntriesPerPageFormOptionImpl extends AbstractFormOption<Integer> implements EntriesPerPageFormOption {

    EntriesPerPageFormOptionImpl(final String fieldLabel, final String fieldValue, final Integer value, final boolean isDefault) {
        super(fieldLabel, fieldValue, value, isDefault);
    }
}
