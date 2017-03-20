package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.breadcrumbs.BreadcrumbViewModel;
import com.commercetools.sunrise.framework.viewmodels.TitleDescriptionViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.FacetSelectorListViewModel;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.WithFacetedSearchViewModel;
import com.commercetools.sunrise.search.pagination.viewmodels.PaginationViewModel;
import com.commercetools.sunrise.search.pagination.viewmodels.EntriesPerPageSelectorViewModel;
import com.commercetools.sunrise.search.pagination.viewmodels.WithPaginationViewModel;
import com.commercetools.sunrise.search.searchbox.viewmodels.WithSearchBoxViewModel;
import com.commercetools.sunrise.search.sort.viewmodels.SortSelectorViewModel;
import com.commercetools.sunrise.search.sort.viewmodels.WithSortSelectorViewModel;

public class ProductOverviewPageContent extends PageContent implements WithPaginationViewModel, WithSearchBoxViewModel, WithSortSelectorViewModel, WithFacetedSearchViewModel {

    private String filterProductsUrl;
    private String searchTerm;
    private BannerViewModel banner;
    private JumbotronViewModel jumbotron;
    private TitleDescriptionViewModel seo;
    private BreadcrumbViewModel breadcrumb;
    private FacetSelectorListViewModel facets;
    private PaginationViewModel pagination;
    private EntriesPerPageSelectorViewModel displaySelector;
    private SortSelectorViewModel sortSelector;
    private ProductListViewModel products;

    public ProductOverviewPageContent() {
    }

    public String getFilterProductsUrl() {
        return filterProductsUrl;
    }

    public void setFilterProductsUrl(final String filterProductsUrl) {
        this.filterProductsUrl = filterProductsUrl;
    }

    @Override
    public String getSearchTerm() {
        return searchTerm;
    }

    @Override
    public void setSearchTerm(final String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public BannerViewModel getBanner() {
        return banner;
    }

    public void setBanner(final BannerViewModel banner) {
        this.banner = banner;
    }

    public JumbotronViewModel getJumbotron() {
        return jumbotron;
    }

    public void setJumbotron(final JumbotronViewModel jumbotron) {
        this.jumbotron = jumbotron;
    }

    public TitleDescriptionViewModel getSeo() {
        return seo;
    }

    public void setSeo(final TitleDescriptionViewModel seo) {
        this.seo = seo;
    }

    public BreadcrumbViewModel getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(final BreadcrumbViewModel breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    @Override
    public FacetSelectorListViewModel getFacets() {
        return facets;
    }

    @Override
    public void setFacets(final FacetSelectorListViewModel facets) {
        this.facets = facets;
    }

    @Override
    public PaginationViewModel getPagination() {
        return pagination;
    }

    @Override
    public void setPagination(final PaginationViewModel pagination) {
        this.pagination = pagination;
    }

    @Override
    public EntriesPerPageSelectorViewModel getDisplaySelector() {
        return displaySelector;
    }

    @Override
    public void setDisplaySelector(final EntriesPerPageSelectorViewModel displaySelector) {
        this.displaySelector = displaySelector;
    }

    @Override
    public SortSelectorViewModel getSortSelector() {
        return sortSelector;
    }

    @Override
    public void setSortSelector(final SortSelectorViewModel sortSelector) {
        this.sortSelector = sortSelector;
    }

    public ProductListViewModel getProducts() {
        return products;
    }

    public void setProducts(final ProductListViewModel products) {
        this.products = products;
    }
}