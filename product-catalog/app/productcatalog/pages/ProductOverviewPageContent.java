package productcatalog.pages;

import common.pages.PageContent;
import common.pages.SelectableData;
import common.pages.SelectableLinkData;
import io.sphere.sdk.products.ProductProjection;
import productcatalog.models.SortOption;

import java.util.List;

public class ProductOverviewPageContent extends PageContent {
    private final String additionalTitle;
    private final List<SelectableLinkData> breadcrumb;
    private final ProductListData productListData;
    private final FilterListData filterListData;
    private final List<SortOption<ProductProjection>> sortOptions;
    private final PaginationData paginationData;
    private final JumbotronData jumbotronData;
    private final List<SelectableData> displayOptions;

    public ProductOverviewPageContent(final String additionalTitle, final List<SelectableLinkData> breadcrumb, final ProductListData productListData,
                                      final FilterListData filterListData, final List<SortOption<ProductProjection>> sortOptions,
                                      final PaginationData paginationData, final List<SelectableData> displayOptions,
                                      final JumbotronData jumbotronData) {
        this.additionalTitle = additionalTitle;
        this.breadcrumb = breadcrumb;
        this.productListData = productListData;
        this.filterListData = filterListData;
        this.sortOptions = sortOptions;
        this.paginationData = paginationData;
        this.displayOptions = displayOptions;
        this.jumbotronData = jumbotronData;
    }

    @Override
    public String additionalTitle() {
        return additionalTitle;
    }

    public List<SelectableLinkData> getBreadcrumb() {
        return breadcrumb;
    }

    public FilterListData getFilters() {
        return filterListData;
    }

    public ProductListData getProducts() {
        return productListData;
    }

    public List<SortOption<ProductProjection>> getSort() {
        return sortOptions;
    }

    public List<SelectableData> getDisplay() {
        return displayOptions;
    }

    public PaginationData getPagination() {
        return paginationData;
    }

    public JumbotronData getJumbotron() {
        return jumbotronData;
    }
}