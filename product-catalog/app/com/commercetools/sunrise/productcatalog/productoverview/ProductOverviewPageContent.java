package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.models.InfoData;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.productcatalog.common.BreadcrumbBean;
import com.commercetools.sunrise.productcatalog.common.ProductListBean;
import com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage.ProductsPerPageSelectorBean;
import com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.FacetSelectorListBean;
import com.commercetools.sunrise.productcatalog.productoverview.search.sort.SortSelectorBean;

public class ProductOverviewPageContent extends PageContent {

    private String filterProductsUrl;
    private String searchTerm;
    private BannerBean banner;
    private JumbotronBean jumbotron;
    // TODO searchResult
    private InfoData seo;
    private BreadcrumbBean breadcrumb;
    private FacetSelectorListBean facets;
    private PaginationBean pagination;
    private ProductsPerPageSelectorBean displaySelector;
    private SortSelectorBean sortSelector;
    private ProductListBean products;
    // TODO wishlist

    public ProductOverviewPageContent() {
    }

    public String getFilterProductsUrl() {
        return filterProductsUrl;
    }

    public void setFilterProductsUrl(final String filterProductsUrl) {
        this.filterProductsUrl = filterProductsUrl;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

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

    public InfoData getSeo() {
        return seo;
    }

    public void setSeo(final InfoData seo) {
        this.seo = seo;
    }

    public BreadcrumbBean getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(final BreadcrumbBean breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public FacetSelectorListBean getFacets() {
        return facets;
    }

    public void setFacets(final FacetSelectorListBean facets) {
        this.facets = facets;
    }

    public PaginationBean getPagination() {
        return pagination;
    }

    public void setPagination(final PaginationBean pagination) {
        this.pagination = pagination;
    }

    public ProductsPerPageSelectorBean getDisplaySelector() {
        return displaySelector;
    }

    public void setDisplaySelector(final ProductsPerPageSelectorBean displaySelector) {
        this.displaySelector = displaySelector;
    }

    public SortSelectorBean getSortSelector() {
        return sortSelector;
    }

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