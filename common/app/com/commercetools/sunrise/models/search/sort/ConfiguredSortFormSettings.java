package com.commercetools.sunrise.models.search.sort;

import com.commercetools.sunrise.core.viewmodels.forms.WithFormFieldName;
import com.commercetools.sunrise.core.viewmodels.forms.WithFormOptions;

import java.util.List;

public interface ConfiguredSortFormSettings extends WithFormFieldName, WithFormOptions<ConfiguredSortFormOption, List<String>> {

    static ConfiguredSortFormSettings of(final String fieldName, final List<ConfiguredSortFormOption> options) {
        return new ConfiguredSortFormSettingsImpl(fieldName, options);
    }
}
