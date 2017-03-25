package com.commercetools.sunrise.search.pagination;

import com.commercetools.sunrise.framework.viewmodels.forms.FormSettings;
import play.mvc.Http;

import javax.annotation.Nullable;

public interface PaginationSettings extends FormSettings<Long> {

    /**
     * The amount of pages on each side of the current page that are can be displayed.
     * @return amount of displayed pages
     */
    int getDisplayedPages();

    default long getOffset(final Http.Context httpContext, final long limit) {
        return (getSelectedValue(httpContext) - 1) * limit;
    }

    @Nullable
    @Override
    default Long mapFieldValueToValue(final String fieldValue) {
        try {
            return Long.valueOf(fieldValue);
        } catch (NumberFormatException e) {
            return null;
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