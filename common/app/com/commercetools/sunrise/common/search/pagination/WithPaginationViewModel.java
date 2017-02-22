package com.commercetools.sunrise.common.search.pagination;

public interface WithPaginationViewModel {

    PaginationViewModel getPagination();

    void setPagination(final PaginationViewModel pagination);

    ProductsPerPageSelectorViewModel getDisplaySelector();

    void setDisplaySelector(final ProductsPerPageSelectorViewModel displaySelector);
}
