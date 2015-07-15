package productcatalog.pages;

import common.cms.CmsPage;
import common.contexts.AppContext;
import common.pages.PageContent;
import common.utils.PriceFormatter;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;

import static java.util.Arrays.asList;

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
        return "Products";
    }

    public List<ImageData> getGallery() {
        return asList(new ImageData(), new ImageData(), new ImageData(), new ImageData());
    }

    public ProductData getProduct() {
        return new ProductData(product, context);
    }

    public SuggestionListData getSuggestions() {
        return new SuggestionListData(suggestionList, context, priceFormatter);
    }

    public String getReviews() {
        return "reviews";
    }
}
