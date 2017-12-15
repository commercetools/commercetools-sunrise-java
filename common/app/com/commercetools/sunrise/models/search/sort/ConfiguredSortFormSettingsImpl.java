package com.commercetools.sunrise.models.search.sort;

import com.commercetools.sunrise.core.viewmodels.forms.AbstractFormSettingsWithOptions;

import java.util.List;

class ConfiguredSortFormSettingsImpl extends AbstractFormSettingsWithOptions<ConfiguredSortFormOption, List<String>> implements ConfiguredSortFormSettings {

    ConfiguredSortFormSettingsImpl(final String fieldName, final List<ConfiguredSortFormOption> options) {
        super(fieldName, options);
    }
}
