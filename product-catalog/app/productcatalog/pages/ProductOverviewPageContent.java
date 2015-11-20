package productcatalog.pages;

import common.pages.PageContent;
import common.models.SelectableData;
import io.sphere.sdk.products.ProductProjection;
import productcatalog.models.SortOption;

import java.util.List;

public class ProductOverviewPageContent extends PageContent {
    private String additionalTitle;
    private BreadcrumbData breadcrumb;
    private ProductListData productListData;
    private FilterListData filterListData;
    private List<SortOption<ProductProjection>> sortOptions;
    private PaginationData paginationData;
    private JumbotronData jumbotronData;
    private List<SelectableData> displayOptions;

    public ProductOverviewPageContent() {
    }

    public ProductOverviewPageContent(final String additionalTitle) {
        this.additionalTitle = additionalTitle;
    }

    @Override
    public String additionalTitle() {
        return additionalTitle;
    }

    public BreadcrumbData getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(final BreadcrumbData breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public ProductListData getProductListData() {
        return productListData;
    }

    public void setProductListData(final ProductListData productListData) {
        this.productListData = productListData;
    }

    public FilterListData getFilterListData() {
        return filterListData;
    }

    public void setFilterListData(final FilterListData filterListData) {
        this.filterListData = filterListData;
    }

    public List<SortOption<ProductProjection>> getSortOptions() {
        return sortOptions;
    }

    public void setSortOptions(final List<SortOption<ProductProjection>> sortOptions) {
        this.sortOptions = sortOptions;
    }

    public PaginationData getPaginationData() {
        return paginationData;
    }

    public void setPaginationData(final PaginationData paginationData) {
        this.paginationData = paginationData;
    }

    public JumbotronData getJumbotronData() {
        return jumbotronData;
    }

    public void setJumbotronData(final JumbotronData jumbotronData) {
        this.jumbotronData = jumbotronData;
    }

    public List<SelectableData> getDisplayOptions() {
        return displayOptions;
    }

    public void setDisplayOptions(final List<SelectableData> displayOptions) {
        this.displayOptions = displayOptions;
    }
}