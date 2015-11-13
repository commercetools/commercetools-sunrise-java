package productcatalog.pages;

import common.pages.LinkData;
import common.pages.SelectableLinkData;
import io.sphere.sdk.models.Base;

import java.util.List;

public class PaginationData extends Base {
    private final int totalProducts;
    private final int productsCount;
    private List<SelectableLinkData> pages;
    private LinkData prevPage;
    private LinkData nextPage;
    private LinkData firstPage;
    private LinkData lastPage;

    public PaginationData(final int productsCount, final int totalProducts) {
        this.productsCount = productsCount;
        this.totalProducts = totalProducts;
    }

    public int getProductsCount() {
        return productsCount;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public List<SelectableLinkData> getPages() {
        return pages;
    }

    public void setPages(final List<SelectableLinkData> pages) {
        this.pages = pages;
    }

    public LinkData getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(final LinkData prevPage) {
        this.prevPage = prevPage;
    }

    public LinkData getNextPage() {
        return nextPage;
    }

    public void setNextPage(final LinkData nextPage) {
        this.nextPage = nextPage;
    }

    public LinkData getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(final LinkData firstPage) {
        this.firstPage = firstPage;
    }

    public LinkData getLastPage() {
        return lastPage;
    }

    public void setLastPage(final LinkData lastPage) {
        this.lastPage = lastPage;
    }
}
