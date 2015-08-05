package productcatalog.pages;

import common.cms.CmsPage;
import common.pages.ImageData;
import common.pages.LinkData;
import common.pages.PageContent;

import java.util.List;

public class ProductDetailPageContent extends PageContent {

    private final String additionalTitle;
    private final String text;
    private final List<LinkData> breadcrumb;
    private final List<ImageData> gallery;
    private final ProductData product;
    private final ProductListData suggestions;

    public ProductDetailPageContent(final CmsPage cms, final String additionalTitle, final String text, final List<LinkData> breadcrumb, final List<ImageData> gallery, final ProductData product, final ProductListData suggestions) {
        super(cms);
        this.additionalTitle = additionalTitle;
        this.text = text;
        this.breadcrumb = breadcrumb;
        this.gallery = gallery;
        this.product = product;
        this.suggestions = suggestions;
    }

    @Override
    public String additionalTitle() {
        return additionalTitle;
    }

    public String getText() {
        return text;
    }

    public List<LinkData> getBreadcrumb() {
        return breadcrumb;
    }

    public List<ImageData> getGallery() {
        return gallery;
    }

    public ProductData getProduct() {
        return product;
    }

    public ProductListData getSuggestions() {
        return suggestions;
    }
}