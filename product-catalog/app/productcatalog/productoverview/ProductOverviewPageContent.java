package productcatalog.productoverview;

import common.models.DetailData;
import common.controllers.PageContent;
import productcatalog.common.BreadcrumbBean;
import productcatalog.common.ProductListData;
import productcatalog.productoverview.search.DisplaySelectorBean;
import productcatalog.productoverview.search.FacetBeanList;
import productcatalog.productoverview.search.SortSelectorBean;

public class ProductOverviewPageContent extends PageContent {
    private String filterProductsUrl;
    private String searchTerm;
    private BannerBean banner;
    private JumbotronBean jumbotron;
    // TODO searchResult
    private DetailData seo;
    private BreadcrumbBean breadcrumb;
    private FacetBeanList facets;
    private PaginationBean pagination;
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

    public DetailData getSeo() {
        return seo;
    }

    public void setSeo(final DetailData seo) {
        this.seo = seo;
    }

    public BreadcrumbBean getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(final BreadcrumbBean breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public FacetBeanList getFacets() {
        return facets;
    }

    public void setFacets(final FacetBeanList facets) {
        this.facets = facets;
    }

    public PaginationBean getPagination() {
        return pagination;
    }

    public void setPagination(final PaginationBean pagination) {
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