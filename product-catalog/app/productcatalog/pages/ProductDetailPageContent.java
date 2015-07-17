package productcatalog.pages;

import common.cms.CmsPage;
import common.contexts.AppContext;
import common.pages.*;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import io.sphere.sdk.productdiscounts.DiscountedPrice;
import io.sphere.sdk.products.Price;
import io.sphere.sdk.products.ProductProjection;

import javax.money.MonetaryAmount;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static common.utils.Languages.withSuitableLanguage;

public class ProductDetailPageContent extends PageContent {
    private final AppContext context;
    private final PriceFinder priceFinder;
    private final PriceFormatter priceFormatter;
    private final ProductProjection product;
    private final List<ProductProjection> suggestionList;
    private final Optional<Price> priceOpt;

    public ProductDetailPageContent(final CmsPage cms, final AppContext context, final ProductProjection product, List<ProductProjection> suggestionList, final PriceFinder priceFinder, final PriceFormatter priceFormatter) {
        super(cms);
        this.context = context;
        this.priceFinder = priceFinder;
        this.priceFormatter = priceFormatter;
        this.product = product;
        this.suggestionList = suggestionList;
        this.priceOpt = priceFinder.findPrice(product.getMasterVariant().getPrices());
    }

    @Override
    public String additionalTitle() {
        return withSuitableLanguage(product.getName(), context);
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
        return ProductDataBuilder.of()
                .withText(withSuitableLanguage(product.getName(), context))
                .withSku(product.getMasterVariant().getSku().orElse(""))
                .withRatingList(buildRating())
                .withDescription(product.getDescription().map(description -> withSuitableLanguage(description, context)).orElse(""))
                .withViewDetailsText(cms.getOrEmpty("product.viewDetails"))
                .withPrice(getFormattedPrice())
                .withPriceOld(getFormattedPriceOld())
                .withColorList(buildColorList())
                .withSizeList(buildSizeList())
                .withSizeGuideText(cms.getOrEmpty("product.sizeGuide"))
                .withBagItemList(buildBagItemList())
                .withAddToBagText(cms.getOrEmpty("product.addToBag"))
                .withAddToWishlistText(cms.getOrEmpty("product.addToWishlist"))
                .withAvailableText(cms.getOrEmpty("product.available"))
                .withProductDetails(buildProductDetails())
                .withDeliveryAndReturn(buildDeliveryAndReturn())
                .build();
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

    private CollectionData buildRating() {
        return new CollectionData("4/5", Arrays.asList(
                new SelectableData("5 Stars", "5", cms.getOrEmpty("product.ratingList.five.text"), "", false),
                new SelectableData("4 Stars", "4", cms.getOrEmpty("product.ratingList.four.text"), "", false),
                new SelectableData("3 Stars", "3", cms.getOrEmpty("product.ratingList.three.text"), "", false),
                new SelectableData("2 Stars", "2", cms.getOrEmpty("product.ratingList.two.text"), "", false),
                new SelectableData("1 Stars", "1", cms.getOrEmpty("product.ratingList.one.text"), "", false)
        ));
    }

    private CollectionData buildColorList() {
        return new CollectionData(cms.getOrEmpty("product.colorList.text"), Arrays.asList(
                new SelectableData(cms.getOrEmpty("product.colorList.choose.text"), "none", "", "", true),
                new SelectableData("Blue", "blue", "", "", false),
                new SelectableData("Red", "red", "", "", false)
        ));
    }

    private CollectionData buildSizeList() {
        return new CollectionData(cms.getOrEmpty("product.sizeList.text"), Arrays.asList(
                new SelectableData(cms.getOrEmpty("product.sizeList.choose.text"), "none", "", "", true),
                new SelectableData("l", "l", "", "", false),
                new SelectableData("m", "m", "", "", false),
                new SelectableData("s", "s", "", "", false)
        ));
    }

    private CollectionData buildBagItemList() {
        return new CollectionData("", Arrays.asList(
                new SelectableData("1", "", "", "", true),
                new SelectableData("2", "", "", "", false),
                new SelectableData("3", "", "", "", false),
                new SelectableData("4", "", "", "", false),
                new SelectableData("5", "", "", "", false),
                new SelectableData("6", "", "", "", false),
                new SelectableData("7", "", "", "", false),
                new SelectableData("8", "", "", "", false),
                new SelectableData("9", "", "", "", false)
        ));
    }

    private DetailData buildProductDetails() {
        return new DetailData(cms.getOrEmpty("product.productDetails.text"), "Some Description");
    }


    private DetailData buildDeliveryAndReturn() {
        return new DetailData(cms.getOrEmpty("product.deliveryAndReturn.text"), "Some Description");
    }

    private String getFormattedPrice() {
        final Optional<MonetaryAmount> currentPrice = priceOpt.map(price -> price.getDiscounted()
                .map(DiscountedPrice::getValue)
                .orElse(price.getValue()));

        return currentPrice.map(price -> priceFormatter.format(price, context)).orElse("");
    }

    private String getFormattedPriceOld() {
        final Optional<MonetaryAmount> oldPrice = priceOpt.flatMap(price -> price.getDiscounted()
                .map(d -> price.getValue()));

        return oldPrice.map(price -> priceFormatter.format(price, context)).orElse("");
    }
}