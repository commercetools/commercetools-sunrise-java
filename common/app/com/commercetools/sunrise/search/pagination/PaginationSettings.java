package com.commercetools.sunrise.search.pagination;

import com.commercetools.sunrise.framework.viewmodels.forms.FormSettingsWithDefault;
import play.mvc.Http;

import javax.annotation.Nullable;
import java.util.Optional;

public interface PaginationSettings extends FormSettingsWithDefault<Long> {

    /**
     * The amount of pages on each side of the current page that are can be displayed.
     * @return amount of displayed pages
     */
    int getDisplayedPages();

    default long getOffset(final Http.Context httpContext, final long limit) {
        return (getSelectedValueOrDefault(httpContext) - 1) * limit;
    }

    @Override
    default Optional<Long> mapFieldValueToValue(final String fieldValue) {
        try {
            return Optional.ofNullable(Long.valueOf(fieldValue));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Override
    default boolean isValidValue(@Nullable final Long value) {
        return value != null && value > 0;
    }

    static PaginationSettings of(final String fieldName, final int displayedPages) {
        return new PaginationSettingsImpl(fieldName, displayedPages);
    }
}