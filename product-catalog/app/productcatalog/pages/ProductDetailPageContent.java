package productcatalog.pages;

import common.cms.CmsPage;
import common.contexts.AppContext;
import common.pages.CollectionData;
import common.pages.ImageData;
import common.pages.PageContent;
import common.pages.SelectableData;
import common.utils.PriceFormatter;
import io.sphere.sdk.products.ProductProjection;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDetailPageContent extends PageContent {
    private final AppContext context;
    private final PriceFormatter priceFormatter;
    private final ProductProjection product;
    private final List<ProductProjection> suggestionList;

    public ProductDetailPageContent(final CmsPage cms, final AppContext context, final ProductProjection product, List<ProductProjection> suggestionList, final PriceFormatter priceFormatter) {
        super(cms);
        this.context = context;
        this.priceFormatter = priceFormatter;
        this.product = product;
        this.suggestionList = suggestionList;
    }

    @Override
    public String additionalTitle() {
        // TODO Fill with category name?
        return "";
    }

    public String getText() {
        return cms.getOrEmpty("content.text");
    }

    public List<ImageData> getGallery() {
        return product.getMasterVariant().getImages().stream()
                .map(image -> ImageData.of(image))
                .collect(Collectors.toList());
    }

    public ProductData getProduct() {
        return new ProductData(product, context);
    }

    public ProductListData getSuggestions() {
        return new ProductListData(suggestionList, context, priceFormatter,
                cms.getOrEmpty("suggestions.text"), cms.getOrEmpty("suggestions.sale"),
                cms.getOrEmpty("suggestions.new"), cms.getOrEmpty("suggestions.quickView"),
                cms.getOrEmpty("suggestions.wishlist"), cms.getOrEmpty("suggestions.moreColors"));
    }

    public CollectionData getReviews() {
        return new CollectionData(cms.getOrEmpty("reviews.text"), Collections.<SelectableData>emptyList());
    }
}
