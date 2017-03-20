package com.commercetools.sunrise.search.pagination;

import com.commercetools.sunrise.framework.SunriseModel;
import play.Configuration;

public class PaginationSettingsFactory extends SunriseModel {

    private static final String CONFIG_FIELD_NAME = "fieldName";
    private static final String DEFAULT_FIELD_NAME = "page";
    private static final String CONFIG_DISPLAYED_PAGES = "displayedPages";
    private static final int DEFAULT_DISPLAYED_PAGES = 2;

    public PaginationSettings create(final Configuration configuration) {
        final String fieldName = fieldName(configuration);
        final int displayedPages = displayedPages(configuration);
        return new PaginationSettingsImpl(fieldName, displayedPages);
    }

    protected final String fieldName(final Configuration configuration) {
        return configuration.getString(CONFIG_FIELD_NAME, DEFAULT_FIELD_NAME);
    }

    protected final int displayedPages(final Configuration configuration) {
        return configuration.getInt(CONFIG_DISPLAYED_PAGES, DEFAULT_DISPLAYED_PAGES);
    }
}