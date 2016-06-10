package com.commercetools.sunrise.productcatalog.productoverview.search.pagination;

import common.contexts.RequestContext;
import io.sphere.sdk.models.Base;
import play.Configuration;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.commercetools.sunrise.productcatalog.productoverview.search.SearchUtils.selectedValues;

public class PaginationFactory extends Base {

    public static final int DEFAULT_PAGE = 1;
    private String key;
    @Inject
    private RequestContext requestContext;

    @Inject
    public void setConfiguration(final Configuration configuration) {
        this.key = configuration.getString("pop.pagination.key", "page");
    }

    @Nonnull
    public Pagination create() {
        return Pagination.of(key, selectedPage());
    }

    private int selectedPage() {
        return selectedValues(key, requestContext).stream()
                .findFirst()
                .map(page -> {
                    try {
                        return Integer.valueOf(page);
                    } catch (NumberFormatException e) {
                        return DEFAULT_PAGE;
                    }
                })
                .orElse(DEFAULT_PAGE);
    }
}
