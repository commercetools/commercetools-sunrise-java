package com.commercetools.sunrise.search.searchbox;

import com.commercetools.sunrise.framework.viewmodels.forms.FormSettings;
import io.sphere.sdk.models.LocalizedStringEntry;
import play.mvc.Http;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Optional;

public interface SearchBoxSettings extends FormSettings<String> {

    default Optional<LocalizedStringEntry> getSearchText(final Http.Context httpContext, final Locale locale) {
        return getSelectedValue(httpContext)
                .map(text -> LocalizedStringEntry.of(locale, text));
    }

    @Override
    default Optional<String> mapFieldValueToValue(final String fieldValue) {
        return Optional.of(fieldValue);
    }

    @Override
    default boolean isValidValue(@Nullable final String value) {
        return value != null && !value.trim().isEmpty();
    }

    static SearchBoxSettings of(final String fieldName) {
        return new SearchBoxSettingsImpl(fieldName);
    }
}