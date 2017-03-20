package com.commercetools.sunrise.search.pagination;

import com.commercetools.sunrise.framework.viewmodels.forms.FormOption;
import com.commercetools.sunrise.framework.viewmodels.forms.FormSettingsWithOptions;
import play.mvc.Http;

import java.util.List;

public interface EntriesPerPageFormSettings extends FormSettingsWithOptions<EntriesPerPageFormOption, Integer> {

    /**
     * Default limit as defined in the CTP platform
     */
    int DEFAULT_LIMIT = 20;

    default long getLimit(final Http.Context httpContext) {
        return getSelectedOption(httpContext)
                .map(FormOption::getValue)
                .orElse(DEFAULT_LIMIT);
    }

    static EntriesPerPageFormSettings of(final String fieldName, final List<EntriesPerPageFormOption> options) {
        return new EntriesPerPageFormSettingsImpl(fieldName, options);
    }
}
