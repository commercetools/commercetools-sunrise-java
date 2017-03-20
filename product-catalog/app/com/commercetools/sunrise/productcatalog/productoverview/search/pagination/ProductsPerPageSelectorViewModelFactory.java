package com.commercetools.sunrise.productcatalog.productoverview.search.pagination;

import com.commercetools.sunrise.search.pagination.viewmodels.AbstractEntriesPerPageSelectorViewModelFactory;
import com.commercetools.sunrise.search.pagination.viewmodels.EntriesPerPageFormSelectableOptionViewModelFactory;
import play.mvc.Http;

import javax.inject.Inject;

public final class ProductsPerPageSelectorViewModelFactory extends AbstractEntriesPerPageSelectorViewModelFactory {

    @Inject
    public ProductsPerPageSelectorViewModelFactory(final ProductsPerPageFormSettings settings,
                                                   final EntriesPerPageFormSelectableOptionViewModelFactory entriesPerPageFormSelectableOptionViewModelFactory,
                                                   final Http.Context httpContext) {
        super(settings, entriesPerPageFormSelectableOptionViewModelFactory, httpContext);
    }
}
