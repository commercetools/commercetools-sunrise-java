package com.commercetools.sunrise.common.search.pagination;

public interface WithPaginationViewModel {

    PaginationBean getPagination();

    void setPagination(final PaginationBean pagination);

    ProductsPerPageSelectorBean getDisplaySelector();

    void setDisplaySelector(final ProductsPerPageSelectorBean displaySelector);
}
