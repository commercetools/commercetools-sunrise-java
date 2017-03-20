package com.commercetools.sunrise.search.sort;

import com.commercetools.sunrise.framework.viewmodels.forms.WithFormFieldName;
import com.commercetools.sunrise.framework.viewmodels.forms.WithFormOptions;

import java.util.List;

public interface ConfiguredSortFormSettings extends WithFormFieldName, WithFormOptions<ConfiguredSortFormOption, List<String>> {

    static ConfiguredSortFormSettings of(final String fieldName, final List<ConfiguredSortFormOption> options) {
        return new ConfiguredSortFormSettingsImpl(fieldName, options);
    }
}
