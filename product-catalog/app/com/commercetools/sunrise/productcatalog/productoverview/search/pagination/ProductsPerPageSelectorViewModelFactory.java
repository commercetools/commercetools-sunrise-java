package com.commercetools.sunrise.productcatalog.productoverview.search.pagination;

import com.commercetools.sunrise.search.pagination.viewmodels.AbstractEntriesPerPageSelectorViewModelFactory;
import com.commercetools.sunrise.search.pagination.viewmodels.EntriesPerPageFormSelectableOptionViewModelFactory;

import javax.inject.Inject;

public final class ProductsPerPageSelectorViewModelFactory extends AbstractEntriesPerPageSelectorViewModelFactory {

    @Inject
    public ProductsPerPageSelectorViewModelFactory(final ProductsPerPageFormSettings productsPerPageFormSettings,
                                                   final EntriesPerPageFormSelectableOptionViewModelFactory entriesPerPageFormSelectableOptionViewModelFactory) {
        super(productsPerPageFormSettings, entriesPerPageFormSelectableOptionViewModelFactory);
    }
}
