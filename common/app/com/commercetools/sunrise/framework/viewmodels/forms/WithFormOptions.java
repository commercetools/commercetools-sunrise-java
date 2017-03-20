package com.commercetools.sunrise.framework.viewmodels.forms;

import java.util.List;
import java.util.Optional;

public interface WithFormOptions<T extends FormOption<V>, V> {

    List<T> getOptions();

    /**
     * Finds the default option, i.e. the first option marked as default.
     * In case none is marked as default, it returns the first option.
     * @return the first default option, or the first option if no default option is defined.
     */
    default Optional<T> findDefaultOption() {
        final Optional<T> defaultOption = getOptions().stream()
                .filter(FormOption::isDefault)
                .findFirst();
        if (defaultOption.isPresent()) {
            return defaultOption;
        } else {
            return getOptions().stream().findFirst();
        }
    }
}
