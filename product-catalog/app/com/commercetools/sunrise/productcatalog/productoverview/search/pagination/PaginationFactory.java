package com.commercetools.sunrise.productcatalog.productoverview.search.pagination;

import com.commercetools.sunrise.common.contexts.RequestContext;
import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.pagination.Pagination;
import io.sphere.sdk.models.Base;
import play.Configuration;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.commercetools.sunrise.productcatalog.productoverview.search.SearchUtils.selectedValues;

@RequestScoped
public class PaginationFactory extends Base {

    private static final int DEFAULT_PAGE = 1;
    private final String key;
    private final RequestContext requestContext;

    @Inject
    public PaginationFactory(final Configuration configuration, final RequestContext requestContext) {
        this.key = configuration.getString("pop.pagination.key", "page");
        this.requestContext = requestContext;
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
