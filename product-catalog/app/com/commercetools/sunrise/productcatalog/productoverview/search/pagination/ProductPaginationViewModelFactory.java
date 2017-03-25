package com.commercetools.sunrise.productcatalog.productoverview.search.pagination;

import com.commercetools.sunrise.search.pagination.viewmodels.AbstractPaginationViewModelFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class ProductPaginationViewModelFactory extends AbstractPaginationViewModelFactory {

    @Inject
    public ProductPaginationViewModelFactory(final ProductPaginationSettings productPaginationSettings) {
        super(productPaginationSettings);
    }
}
