package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.models.SunriseModel;

import java.util.List;
import java.util.Optional;

public abstract class FormSettingsWithOptions<T extends FormOption> extends SunriseModel implements WithFormFieldName<T> {

    private final String fieldName;
    private final List<T> options;

    protected FormSettingsWithOptions(final String fieldName, final List<T> options) {
        this.fieldName = fieldName;
        this.options = options;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    public List<T> getOptions() {
        return options;
    }

    /**
     * Finds the default option, i.e. the first option marked as default.
     * In case none is marked as default, it returns the first option.
     * @return the first default option, or the first option if no default option is defined.
     */
    public Optional<T> findDefaultOption() {
        final Optional<T> defaultOption = options.stream()
                .filter(FormOption::isDefault)
                .findFirst();
        if (defaultOption.isPresent()) {
            return defaultOption;
        } else {
            return options.stream().findFirst();
        }
    }
}
