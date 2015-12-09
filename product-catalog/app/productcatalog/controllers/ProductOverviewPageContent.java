package productcatalog.controllers;

import common.models.DetailData;
import common.controllers.PageContent;
import productcatalog.models.*;

public class ProductOverviewPageContent extends PageContent {
    private String filterProductsUrl;
    private String searchTerm;
    private BannerData banner;
    private JumbotronData jumbotron;
    // TODO searchResult
    private DetailData seo;
    private BreadcrumbData breadcrumb;
    private FacetListData facets;
    private PaginationData pagination;
    private DisplaySelector displaySelector;
    private SortSelector sortSelector;
    private ProductListData products;
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

    public BannerData getBanner() {
        return banner;
    }

    public void setBanner(final BannerData banner) {
        this.banner = banner;
    }

    public JumbotronData getJumbotron() {
        return jumbotron;
    }

    public void setJumbotron(final JumbotronData jumbotron) {
        this.jumbotron = jumbotron;
    }

    public DetailData getSeo() {
        return seo;
    }

    public void setSeo(final DetailData seo) {
        this.seo = seo;
    }

    public BreadcrumbData getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(final BreadcrumbData breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public FacetListData getFacets() {
        return facets;
    }

    public void setFacets(final FacetListData facets) {
        this.facets = facets;
    }

    public PaginationData getPagination() {
        return pagination;
    }

    public void setPagination(final PaginationData pagination) {
        this.pagination = pagination;
    }

    public DisplaySelector getDisplaySelector() {
        return displaySelector;
    }

    public void setDisplaySelector(final DisplaySelector displaySelector) {
        this.displaySelector = displaySelector;
    }

    public SortSelector getSortSelector() {
        return sortSelector;
    }

    public void setSortSelector(final SortSelector sortSelector) {
        this.sortSelector = sortSelector;
    }

    public ProductListData getProducts() {
        return products;
    }

    public void setProducts(final ProductListData products) {
        this.products = products;
    }
}