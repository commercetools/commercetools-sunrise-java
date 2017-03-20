package com.commercetools.sunrise.search.pagination.viewmodels;

public interface WithPaginationViewModel {

    PaginationViewModel getPagination();

    void setPagination(final PaginationViewModel pagination);

    EntriesPerPageSelectorViewModel getDisplaySelector();

    void setDisplaySelector(final EntriesPerPageSelectorViewModel displaySelector);
}
