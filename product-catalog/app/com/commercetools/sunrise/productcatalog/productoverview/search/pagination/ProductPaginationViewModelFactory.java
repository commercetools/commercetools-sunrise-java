package com.commercetools.sunrise.productcatalog.productoverview.search.pagination;

import com.commercetools.sunrise.search.pagination.viewmodels.AbstractPaginationViewModelFactory;
import play.mvc.Http;

import javax.inject.Inject;

public final class ProductPaginationViewModelFactory extends AbstractPaginationViewModelFactory {

    @Inject
    public ProductPaginationViewModelFactory(final ProductPaginationSettings settings, final Http.Context httpContext) {
        super(settings, httpContext);
    }
}
