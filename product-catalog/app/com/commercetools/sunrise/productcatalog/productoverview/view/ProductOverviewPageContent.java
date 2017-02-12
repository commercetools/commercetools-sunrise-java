package com.commercetools.sunrise.productcatalog.productoverview.view;

import com.commercetools.sunrise.common.models.TitleDescriptionBean;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.search.facetedsearch.FacetSelectorListBean;
import com.commercetools.sunrise.common.search.facetedsearch.WithFacetedSearchViewModel;
import com.commercetools.sunrise.common.search.pagination.PaginationBean;
import com.commercetools.sunrise.common.search.pagination.WithPaginationViewModel;
import com.commercetools.sunrise.common.search.pagination.ProductsPerPageSelectorBean;
import com.commercetools.sunrise.common.search.searchbox.WithSearchBoxViewModel;
import com.commercetools.sunrise.common.search.sort.SortSelectorBean;
import com.commercetools.sunrise.common.search.sort.WithSortSelectorViewModel;
import com.commercetools.sunrise.productcatalog.common.BreadcrumbBean;
import com.commercetools.sunrise.productcatalog.common.ProductListBean;
import com.commercetools.sunrise.productcatalog.productoverview.view.BannerBean;
import com.commercetools.sunrise.productcatalog.productoverview.view.JumbotronBean;

public class ProductOverviewPageContent extends PageContent implements WithPaginationViewModel, WithSearchBoxViewModel, WithSortSelectorViewModel, WithFacetedSearchViewModel {

    private String filterProductsUrl;
    private String searchTerm;
    private BannerBean banner;
    private JumbotronBean jumbotron;
    private TitleDescriptionBean seo;
    private BreadcrumbBean breadcrumb;
    private FacetSelectorListBean facets;
    private PaginationBean pagination;
    private ProductsPerPageSelectorBean displaySelector;
    private SortSelectorBean sortSelector;
    private ProductListBean products;

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

    public BannerBean getBanner() {
        return banner;
    }

    public void setBanner(final BannerBean banner) {
        this.banner = banner;
    }

    public JumbotronBean getJumbotron() {
        return jumbotron;
    }

    public void setJumbotron(final JumbotronBean jumbotron) {
        this.jumbotron = jumbotron;
    }

    public TitleDescriptionBean getSeo() {
        return seo;
    }

    public void setSeo(final TitleDescriptionBean seo) {
        this.seo = seo;
    }

    public BreadcrumbBean getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(final BreadcrumbBean breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    @Override
    public FacetSelectorListBean getFacets() {
        return facets;
    }

    @Override
    public void setFacets(final FacetSelectorListBean facets) {
        this.facets = facets;
    }

    @Override
    public PaginationBean getPagination() {
        return pagination;
    }

    @Override
    public void setPagination(final PaginationBean pagination) {
        this.pagination = pagination;
    }

    @Override
    public ProductsPerPageSelectorBean getDisplaySelector() {
        return displaySelector;
    }

    @Override
    public void setDisplaySelector(final ProductsPerPageSelectorBean displaySelector) {
        this.displaySelector = displaySelector;
    }

    @Override
    public SortSelectorBean getSortSelector() {
        return sortSelector;
    }

    @Override
    public void setSortSelector(final SortSelectorBean sortSelector) {
        this.sortSelector = sortSelector;
    }

    public ProductListBean getProducts() {
        return products;
    }

    public void setProducts(final ProductListBean products) {
        this.products = products;
    }
}