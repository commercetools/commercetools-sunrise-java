package com.commercetools.sunrise.models.search.sort;

import com.commercetools.sunrise.core.viewmodels.forms.AbstractFormOption;

import java.util.List;

class ConfiguredSortFormOptionImpl extends AbstractFormOption<List<String>> implements ConfiguredSortFormOption {

    ConfiguredSortFormOptionImpl(final String fieldLabel, final String fieldValue, final List<String> value, final boolean isDefault) {
        super(fieldLabel, fieldValue, value, isDefault);
    }
}
