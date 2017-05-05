package com.commercetools.sunrise.search.pagination;

import com.commercetools.sunrise.framework.viewmodels.forms.AbstractFormSettingsWithDefault;

class PaginationSettingsImpl extends AbstractFormSettingsWithDefault<Long> implements PaginationSettings {

    private static final long DEFAULT_PAGE = 1;

    private final int displayedPages;

    PaginationSettingsImpl(final String fieldName, final int displayedPages) {
        super(fieldName, DEFAULT_PAGE);
        this.displayedPages = displayedPages;
    }

    @Override
    public int getDisplayedPages() {
        return displayedPages;
    }
}