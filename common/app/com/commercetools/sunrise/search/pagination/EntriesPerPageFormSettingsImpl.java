package com.commercetools.sunrise.search.pagination;

import com.commercetools.sunrise.framework.viewmodels.forms.AbstractFormSettingsWithOptions;

import java.util.List;

class EntriesPerPageFormSettingsImpl extends AbstractFormSettingsWithOptions<EntriesPerPageFormOption, Integer> implements EntriesPerPageFormSettings {

    EntriesPerPageFormSettingsImpl(final String fieldName, final List<EntriesPerPageFormOption> options) {
        super(fieldName, options);
    }
}
