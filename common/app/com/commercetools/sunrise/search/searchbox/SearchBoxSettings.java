package com.commercetools.sunrise.search.searchbox;

import com.commercetools.sunrise.framework.viewmodels.forms.FormSettings;
import io.sphere.sdk.models.LocalizedStringEntry;
import play.mvc.Http;

import javax.annotation.Nullable;
import java.util.Locale;

public interface SearchBoxSettings extends FormSettings<String> {

    default LocalizedStringEntry getSearchText(final Http.Context httpContext, final Locale locale) {
        final String selectedValue = this.getSelectedValue(httpContext);
        return LocalizedStringEntry.of(locale, selectedValue);
    }

    @Override
    default String getDefaultValue() {
        return "";
    }

    @Nullable
    @Override
    default String mapFieldValueToValue(final String fieldValue) {
        return fieldValue;
    }

    @Override
    default boolean isValidValue(@Nullable final String value) {
        return value != null;
    }

    static SearchBoxSettings of(final String fieldName) {
        return new SearchBoxSettingsImpl(fieldName);
    }
}