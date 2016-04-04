package productcatalog.productoverview;

import common.models.DetailData;
import common.controllers.PageContent;
import productcatalog.common.BreadcrumbData;
import productcatalog.common.ProductListData;
import productcatalog.productoverview.search.DisplaySelectorBean;
import productcatalog.productoverview.search.FacetBeanList;
import productcatalog.productoverview.search.SortSelectorBean;

public class ProductOverviewPageContent extends PageContent {
    private String filterProductsUrl;
    private String searchTerm;
    private BannerData banner;
    private JumbotronData jumbotron;
    // TODO searchResult
    private DetailData seo;
    private BreadcrumbData breadcrumb;
    private FacetBeanList facets;
    private PaginationData pagination;
    private DisplaySelectorBean displaySelector;
    private SortSelectorBean sortSelector;
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

    public FacetBeanList getFacets() {
        return facets;
    }

    public void setFacets(final FacetBeanList facets) {
        this.facets = facets;
    }

    public PaginationData getPagination() {
        return pagination;
    }

    public void setPagination(final PaginationData pagination) {
        this.pagination = pagination;
    }

    public DisplaySelectorBean getDisplaySelector() {
        return displaySelector;
    }

    public void setDisplaySelector(final DisplaySelectorBean displaySelector) {
        this.displaySelector = displaySelector;
    }

    public SortSelectorBean getSortSelector() {
        return sortSelector;
    }

    public void setSortSelector(final SortSelectorBean sortSelector) {
        this.sortSelector = sortSelector;
    }

    public ProductListData getProducts() {
        return products;
    }

    public void setProducts(final ProductListData products) {
        this.products = products;
    }
}